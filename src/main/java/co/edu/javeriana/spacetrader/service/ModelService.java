package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Model;
import co.edu.javeriana.spacetrader.model.Spaceship;
import co.edu.javeriana.spacetrader.repository.ModelRepository;
import co.edu.javeriana.spacetrader.repository.SpaceshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ModelService {
    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private SpaceshipRepository spaceshipRepository;

    // Retrieve all models
    public List<Model> findAllModels(){ return modelRepository.findAll();}
    //Find Model by ID
    public Model findModelByID(Long id){
        return modelRepository.findById(id).orElseThrow(() -> new RuntimeException("Model not found for this id :: " + id));
    }
    //Save or Update Model
    public Model saveOrUpdateModel(Model model){
       return modelRepository.save(model);
    }
    //Delete Model by ID
    @Transactional
    public void deleteModel(Long id) {
        Model model = findModelByID(id);

        // Attempt to find a default model that is not the one being deleted
        List<Model> models = modelRepository.findAll();
        Model defaultModel = models.stream()
                .filter(m -> !m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No available default model to replace the deleted model."));

        // Find all spaceships referencing the model and reassign them to the default model
        List<Spaceship> spaceships = spaceshipRepository.findByModelId(id);
        for (Spaceship spaceship : spaceships) {
            spaceship.setModel(defaultModel);
            spaceshipRepository.save(spaceship);
        }

        // Delete the model if it is not referenced anymore
        modelRepository.delete(model);
    }



}
