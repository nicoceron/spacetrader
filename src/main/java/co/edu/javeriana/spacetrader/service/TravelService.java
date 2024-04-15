package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Wormhole;
import co.edu.javeriana.spacetrader.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelService {

    @Autowired
    StarRepository starRepository;
}
