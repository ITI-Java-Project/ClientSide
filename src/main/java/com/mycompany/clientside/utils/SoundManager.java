package com.mycompany.clientside.utils;

import java.util.prefs.Preferences;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class SoundManager {

    private static SoundManager instance;
    private final Preferences prefs;

    private MediaPlayer backgroundMusicPlayer;
    private AudioClip clickSound;

    private double masterVolume;
    private double clickVolume;
    private double musicVolume;

    private boolean masterMuted;
    private boolean clickMuted;
    private boolean musicMuted;

    private static final String PREF_MASTER_VOLUME = "masterVolume";
    private static final String PREF_CLICK_VOLUME = "clickVolume";
    private static final String PREF_MUSIC_VOLUME = "musicVolume";
    private static final String PREF_MASTER_MUTED = "masterMuted";
    private static final String PREF_CLICK_MUTED = "clickMuted";
    private static final String PREF_MUSIC_MUTED = "musicMuted";

    private static final String BG_MUSIC_PATH = "/sounds/bgSound.mp3";
    private static final String CLICK_SOUND_PATH = "/sounds/click_sound.wav";

    private SoundManager() {
        prefs = Preferences.userNodeForPackage(SoundManager.class);
        loadSettings();
        initializeSounds();
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void initializeSounds() {
        try {
            String clickSoundUrl = getClass().getResource(CLICK_SOUND_PATH).toExternalForm();
            clickSound = new AudioClip(clickSoundUrl);
            System.out.println("Click sound loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to load click sound: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            String bgMusicUrl = getClass().getResource(BG_MUSIC_PATH).toExternalForm();
            Media bgMusic = new Media(bgMusicUrl);
            backgroundMusicPlayer = new MediaPlayer(bgMusic);
            
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            
            updateMusicVolume();
            
            System.out.println("Background music loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to load background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSettings() {
        masterVolume = prefs.getDouble(PREF_MASTER_VOLUME, 100.0);
        clickVolume = prefs.getDouble(PREF_CLICK_VOLUME, 100.0);
        musicVolume = prefs.getDouble(PREF_MUSIC_VOLUME, 100.0);

        masterMuted = prefs.getBoolean(PREF_MASTER_MUTED, false);
        clickMuted = prefs.getBoolean(PREF_CLICK_MUTED, false);
        musicMuted = prefs.getBoolean(PREF_MUSIC_MUTED, false);
    }

    private void saveSettings() {
        prefs.putDouble(PREF_MASTER_VOLUME, masterVolume);
        prefs.putDouble(PREF_CLICK_VOLUME, clickVolume);
        prefs.putDouble(PREF_MUSIC_VOLUME, musicVolume);

        prefs.putBoolean(PREF_MASTER_MUTED, masterMuted);
        prefs.putBoolean(PREF_CLICK_MUTED, clickMuted);
        prefs.putBoolean(PREF_MUSIC_MUTED, musicMuted);
    }


    public void playBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            updateMusicVolume();
            backgroundMusicPlayer.play();
            System.out.println("Background music started");
        }
    }


    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            System.out.println("Background music stopped");
        }
    }


    public void pauseBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
            System.out.println("Background music paused");
        }
    }

    public void resumeBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            updateMusicVolume();
            backgroundMusicPlayer.play();
            System.out.println("Background music resumed");
        }
    }

    public void playClickSound() {
        if (clickSound != null && getEffectiveClickVolume() > 0) {
            clickSound.setVolume(getEffectiveClickVolume());
            clickSound.play();
        }
    }

    private void updateMusicVolume() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(getEffectiveMusicVolume());
        }
    }


    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double volume) {
        this.masterVolume = clamp(volume);
        saveSettings();
        updateMusicVolume();
    }

    public double getClickVolume() {
        return clickVolume;
    }

    public void setClickVolume(double volume) {
        this.clickVolume = clamp(volume);
        saveSettings();
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = clamp(volume);
        saveSettings();
        updateMusicVolume();
    }


    public boolean isMasterMuted() {
        return masterMuted;
    }

    public void setMasterMuted(boolean muted) {
        this.masterMuted = muted;
        saveSettings();
        updateMusicVolume();
    }

    public boolean isClickMuted() {
        return clickMuted;
    }

    public void setClickMuted(boolean muted) {
        this.clickMuted = muted;
        saveSettings();
    }

    public boolean isMusicMuted() {
        return musicMuted;
    }

    public void setMusicMuted(boolean muted) {
        this.musicMuted = muted;
        saveSettings();
        updateMusicVolume();
    }


    public double getEffectiveClickVolume() {
        if (masterMuted || clickMuted) {
            return 0;
        }
        return (masterVolume / 100.0) * (clickVolume / 100.0);
    }

    public double getEffectiveMusicVolume() {
        if (masterMuted || musicMuted) {
            return 0;
        }
        return (masterVolume / 100.0) * (musicVolume / 100.0);
    }

    private double clamp(double value) {
        return Math.max(0, Math.min(100, value));
    }

    public void cleanup() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.dispose();
        }
    }
}