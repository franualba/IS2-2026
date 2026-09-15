package ar.edu.club.dto;

import ar.edu.club.domain.GrupoFamiliar;

public record GrupoFamiliarDto(String id, String denominacion, int cantidadFamiliares, int cantidadPagos) {
    public static GrupoFamiliarDto from(GrupoFamiliar grupo) {
        return new GrupoFamiliarDto(grupo.getId(), grupo.getDenominacion(), grupo.getFamiliares().size(), grupo.getPagos().size());
    }
}
