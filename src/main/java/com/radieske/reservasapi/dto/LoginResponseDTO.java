package com.radieske.reservasapi.dto;

public record LoginResponseDTO(
    String token,
    String type,
    long expiresIn
)
{
    public LoginResponseDTO(String token, long expiresIn)
    {
        this(token, "Bearer", expiresIn);
    }
}
