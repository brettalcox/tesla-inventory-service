package com.tesla.teslainventoryservice.controller;

import com.tesla.teslainventoryservice.model.TeslaInventory;
import com.tesla.teslainventoryservice.model.TeslaModelRequest;
import com.tesla.teslainventoryservice.service.TeslaInventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/inventory")
@RestController
public class TeslaInventoryController {

    private final TeslaInventoryService teslaInventoryService;

    public TeslaInventoryController(final TeslaInventoryService teslaInventoryService) {
        this.teslaInventoryService = teslaInventoryService;
    }

    @GetMapping
    public List<TeslaInventory> getTeslaInventory(final TeslaModelRequest teslaModelRequest) {
        return teslaInventoryService.getTeslaInventory(teslaModelRequest);
    }
}
