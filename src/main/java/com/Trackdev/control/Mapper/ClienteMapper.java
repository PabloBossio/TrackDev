package com.Trackdev.control.Mapper;

import com.Trackdev.control.Entity.Cliente;
import com.Trackdev.control.Dto.ClienteRequestDTO;
import com.Trackdev.control.Dto.ClienteResponseDTO;
import com.Trackdev.control.Dto.ClienteResumenDTO;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente toEntity(ClienteRequestDTO dto) {
        return Cliente.builder()
                .razonSocial(dto.razonSocial())
                .cuit(dto.cuit())
                .contactoNombre(dto.contactoNombre())
                .contactoEmail(dto.contactoEmail())
                .contactoTelefono(dto.contactoTelefono())
                .build();
    }

    public static void updateEntity(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setRazonSocial(dto.razonSocial());
        cliente.setCuit(dto.cuit());
        cliente.setContactoNombre(dto.contactoNombre());
        cliente.setContactoEmail(dto.contactoEmail());
        cliente.setContactoTelefono(dto.contactoTelefono());
    }

    public static ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getRazonSocial(),
                cliente.getCuit(),
                cliente.getContactoNombre(),
                cliente.getContactoEmail(),
                cliente.getContactoTelefono(),
                cliente.isActivo(),
                cliente.getCreatedAt(),
                cliente.getUpdatedAt()
        );
    }

    public static ClienteResumenDTO toResumenDTO(Cliente cliente) {
        return new ClienteResumenDTO(
                cliente.getId(),
                cliente.getRazonSocial()
        );
    }
}