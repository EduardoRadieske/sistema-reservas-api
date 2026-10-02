package com.radieske.reservasapi.service;

import com.radieske.reservasapi.dto.LoginData;
import com.radieske.reservasapi.dto.LoginResponseDTO;

public interface AuthService
{
	LoginResponseDTO login(LoginData loginData);
}
