package com.musicrecommender.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.musicrecommender.backend.service.MPDImportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;


@RestController
@RequestMapping("admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private MPDImportService mpdImportService;
    private final String mpdSlicesRoot = "C:\\Users\\twelc\\Documents\\Github\\music-recommender\\backend\\data\\data";

    @GetMapping("/import-mpd")
    public String importMPD() {
        File mpdFile = new File(mpdSlicesRoot + "/mpd_import.json");
        boolean success = mpdImportService.importMPD(mpdFile);
        return success ? "Import successful" : "Import failed";
    }

    @GetMapping("/import-all-mpd-slices")
    public String importAllMPDSlices() {
        File rootDir = new File(mpdSlicesRoot);
        mpdImportService.importAllSlices(rootDir);
        return "Batch import started. Check logs for slice results.";
    }

    @GetMapping("/print-all-mpd-playlists")
    public String printAllMPDPlaylists() {
        mpdImportService.printAllPlaylistsInDb();
        return "Printed all MPD playlists to logs.";
    }
}
