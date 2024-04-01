package com.piano.PianoDigital.service.implementations;

import com.piano.PianoDigital.db.repository.UserRepository;
import com.piano.PianoDigital.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

}
