package com.sanyam.CryptoTrading.controller;


import com.sanyam.CryptoTrading.model.Asset;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.repository.AssetRepo;
import com.sanyam.CryptoTrading.service.AssetService;
import com.sanyam.CryptoTrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @PathVariable String coinId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user=userService.findUserByJwt(jwt);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping()
    public ResponseEntity<List<Asset>> getAssetsForUser(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserByJwt(jwt);
        List<Asset> assets = assetService.getUsersAsset(user.getId());
        return ResponseEntity.ok().body(assets);
    }


}
