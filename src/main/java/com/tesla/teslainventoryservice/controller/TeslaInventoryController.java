package com.tesla.teslainventoryservice.controller;

import com.tesla.teslainventoryservice.service.TeslaInventoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/inventory")
@RestController
public class TeslaInventoryController {

    private final TeslaInventoryService teslaInventoryService;

    public TeslaInventoryController(final TeslaInventoryService teslaInventoryService) {
        this.teslaInventoryService = teslaInventoryService;
    }
}
