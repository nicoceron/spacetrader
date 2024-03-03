package co.edu.javeriana.spacetrader.controller;


import co.edu.javeriana.spacetrader.repository.ModelRepository;
import co.edu.javeriana.spacetrader.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelExtensionsKt;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import co.edu.javeriana.spacetrader.model.Model;

import java.util.List;

@Controller
@RequestMapping("/models")
public class ModelController {

    @Autowired
    ModelService modelService;

    //List all models
    @GetMapping("/list")
    public String listModels(ModelMap model){
        List<Model> models = modelService.findAllModels();
        model.addAttribute("models", models);
        return "model-list";
    }
    //Model detail view
    @GetMapping("detail/{id}")
    public String modelDetail(@PathVariable Long id, ModelMap model){
        Model modelEntity = modelService.findModelByID(id);
        model.addAttribute("model", modelEntity);
        return "model-detail";
    }
    //Editing Model
    @GetMapping("/edit-form/{id}")
    public String editModelForm(ModelMap model, @PathVariable Long id){
    Model modelEntity = modelService.findModelByID(id);
    model.addAttribute("model", modelEntity);
    return "model-edit";
    }

    //Save or Update Model
    @PostMapping("/save")
    public String saveOrUpdateModel(@ModelAttribute("model") Model model){
        modelService.saveOrUpdateModel(model);
        return "redirect:/models/list";
    }

    //Delete Model
    @GetMapping("/delete/{id}")
    public String deleteModel(@PathVariable Long id){
        modelService.deleteModel(id);
        return "redirect:/models/list";
    }
}
