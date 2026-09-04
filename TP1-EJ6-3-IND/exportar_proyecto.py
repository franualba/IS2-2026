"""
EXPORTADOR AUTOMÁTICO DEL PROYECTO (compatible con export de chat JSON o TXT).
Uso:  python exportar_proyecto.py <archivo_exportado.json|.txt> [carpeta_destino]

Cómo funciona:
 1. Si el archivo es JSON (export del chat), recorre recursivamente toda la
    estructura y junta todos los textos que contienen marcadores "ARCHIVO:".
    Si es TXT, lo lee directamente.
 2. Detecta los bloques de código delimitados por ``` y, si la primera línea
    del bloque es un marcador  '// ARCHIVO: ruta' | '# ARCHIVO: ruta' |
    '<!-- ARCHIVO: ruta -->',  escribe el contenido en esa ruta.
"""
import re, os, sys, json

PATRON_MARCADOR = re.compile(r"^\s*(?://|#|<!--)\s*ARCHIVO:\s*(.+?)\s*(?:-->)?\s*$")

def leer_texto(ruta):
    """Devuelve el texto plano del chat, sin importar si el export es JSON o TXT."""
    with open(ruta, encoding="utf-8", errors="replace") as f:
        raw = f.read()
    if raw.lstrip()[:1] in ("{", "["):          # parece JSON
        try:
            data = json.loads(raw)
        except json.JSONDecodeError:
            return raw
        textos = []
        def recorrer(nodo):                     # busca todos los strings del JSON
            if isinstance(nodo, dict):
                for v in nodo.values(): recorrer(v)
            elif isinstance(nodo, list):
                for v in nodo: recorrer(v)
            elif isinstance(nodo, str) and "ARCHIVO:" in nodo:
                textos.append(nodo)
        recorrer(data)
        return "\n".join(textos) if textos else raw
    return raw                                   # es TXT: se usa tal cual

def extraer_bloques(texto):
    """Parser por líneas de los bloques ``` ... ``` (más robusto que regex anidadas)."""
    bloques, actual = [], None
    for linea in texto.split("\n"):
        if linea.strip().startswith("```"):
            if actual is None:
                actual = []                      # abre bloque
            else:
                bloques.append(actual)           # cierra bloque
                actual = None
        elif actual is not None:
            actual.append(linea)
    return bloques

def main():
    if len(sys.argv) < 2:
        print("Uso: python exportar_proyecto.py <chat.json|chat.txt> [carpeta]")
        sys.exit(1)
    ruta_origen = sys.argv[1]
    raiz = sys.argv[2] if len(sys.argv) > 2 else "colegio-mvc"

    texto = leer_texto(ruta_origen)
    bloques = extraer_bloques(texto)

    creados = 0
    for bloque in bloques:
        if not bloque:
            continue
        m = PATRON_MARCADOR.match(bloque[0])     # 1ª línea = marcador de ruta
        if not m:
            continue                             # bloque sin marcador: se ignora
        ruta = os.path.join(raiz, m.group(1).strip())
        os.makedirs(os.path.dirname(ruta) or ".", exist_ok=True)
        with open(ruta, "w", encoding="utf-8") as f:
            f.write("\n".join(bloque[1:]).lstrip("\n"))
        print("  ✔ " + m.group(1).strip())
        creados += 1

    if creados == 0:
        print("⚠ No se encontró ningún archivo. Verifica que la exportación",
              "contenga el mensaje completo con los bloques de código.")
    else:
        print(f"\n✅ {creados} archivos exportados en '{raiz}/'.",
              f"\nAbre esa carpeta en tu IDE (IntelliJ/Eclipse/VS Code) como proyecto Maven",
              f"y ejecuta:  mvn spring-boot:run")

if __name__ == "__main__":
    main()