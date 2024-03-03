package co.edu.javeriana.spacetrader.service;

import co.edu.javeriana.spacetrader.model.Model;
import co.edu.javeriana.spacetrader.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ModelService {
    @Autowired
    private ModelRepository modelRepository;

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
    public void deleteModel(Long id){
        Model model = findModelByID(id);
        modelRepository.delete(model);
    }

}
