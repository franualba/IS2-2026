"""
EXPORTADOR AUTOMÁTICO DEL PROYECTO.
Uso:  python exportar_proyecto.py proyecto.md
Lee cada bloque de código del markdown, detecta la primera línea marcadora
('// ARCHIVO: ruta', '# ARCHIVO: ruta' o '<!-- ARCHIVO: ruta -->')
y escribe el contenido en esa ruta dentro de la carpeta 'colegio-mvc/'.
"""
import re, os, sys

def main():
    md   = sys.argv[1] if len(sys.argv) > 1 else "proyecto.md"
    raiz = sys.argv[2] if len(sys.argv) > 2 else "colegio-mvc"
    texto = open(md, encoding="utf-8").read()

    bloques  = re.findall(r"```[\w-]*\n(.*?)```", texto, re.DOTALL)
    patron   = re.compile(r"^\s*(?://|#|<!--)\s*ARCHIVO:\s*(.+?)\s*(?:-->)?\s*$")
    cont = 0
    for b in bloques:
        lineas = b.split("\n")
        m = patron.match(lineas[0])
        if not m:                      # bloque sin marcador (ej: este mismo script)
            continue
        ruta = os.path.join(raiz, m.group(1).strip())
        os.makedirs(os.path.dirname(ruta) or ".", exist_ok=True)
        with open(ruta, "w", encoding="utf-8") as f:
            f.write("\n".join(lineas[1:]).lstrip("\n"))
        print("✔ " + m.group(1))
        cont += 1
    print(f"\n{cont} archivos exportados en '{raiz}/'. Ábrelo en tu IDE como proyecto Maven.")

if __name__ == "__main__":
    main()