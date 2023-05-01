package com.example.projet.service;
import com.example.projet.entity.Bascket;
import com.example.projet.repository.BascketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BascketService {

    private final BascketRepository bascketRepository;

    public BascketService(BascketRepository bascketRepository) {
        this.bascketRepository = bascketRepository;
    }

    /*public Bascket getBascketById(int bascketId) {
        List<Bascket> optionalBascket = bascketRepository.findById(bascketId);
        return optionalBascket.orElse(null);
    }*/

    public List<Bascket> getAllBascet() {
        return bascketRepository.findAll();
    }

    public Bascket saveOrUpdateBascket(Bascket bascket) {
        return bascketRepository.save(bascket);
    }

    public void deleteBascketById(int bascketId) {
        bascketRepository.deleteById(bascketId);
    }
}

