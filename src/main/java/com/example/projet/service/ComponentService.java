package com.example.projet.service;

import com.example.projet.entity.Component;
import com.example.projet.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService {

    private final ComponentRepository componentRepository;

    /*public Component getComponentById(int componentId) {
        Optional<Component> optionalComponent = componentRepository.findById(componentId);
        return optionalComponent.orElse(null);
    }*/
    @Autowired
    public ComponentService(ComponentRepository componentRepository){ this.componentRepository = componentRepository; }

    public List<Component> getAllComponents() {
        return componentRepository.findAll();
    }

    public Component saveOrUpdateComponent(Component component) {
        return componentRepository.save(component);
    }

    public void deleteComponentById(int componentId) {
        componentRepository.deleteById(componentId);
    }

}
