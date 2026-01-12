package com.mycompany.clientside.utils;

import java.util.prefs.Preferences;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Singleton class to manage all sound settings across the application.
 * Settings are persisted using Java Preferences API.
 */
public class SoundManager {

    private static SoundManager instance;
    private final Preferences prefs;

    // Media players
    private MediaPlayer backgroundMusicPlayer;
    private AudioClip clickSound;

    // Volume levels (0-100)
    private double masterVolume;
    private double clickVolume;
    private double musicVolume;

    // Mute states
    private boolean masterMuted;
    private boolean clickMuted;
    private boolean musicMuted;

    // Preference keys
    private static final String PREF_MASTER_VOLUME = "masterVolume";
    private static final String PREF_CLICK_VOLUME = "clickVolume";
    private static final String PREF_MUSIC_VOLUME = "musicVolume";
    private static final String PREF_MASTER_MUTED = "masterMuted";
    private static final String PREF_CLICK_MUTED = "clickMuted";
    private static final String PREF_MUSIC_MUTED = "musicMuted";

    // Sound file paths
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

    /**
     * Initialize all sound resources
     */
    private void initializeSounds() {
        try {
            // Initialize click sound (AudioClip is good for short sounds)
            String clickSoundUrl = getClass().getResource(CLICK_SOUND_PATH).toExternalForm();
            clickSound = new AudioClip(clickSoundUrl);
            System.out.println("Click sound loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to load click sound: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Initialize background music (MediaPlayer is good for longer audio)
            String bgMusicUrl = getClass().getResource(BG_MUSIC_PATH).toExternalForm();
            Media bgMusic = new Media(bgMusicUrl);
            backgroundMusicPlayer = new MediaPlayer(bgMusic);
            
            // Loop the background music indefinitely
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            
            // Set initial volume
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

    // ==================== PLAYBACK CONTROLS ====================

    /**
     * Start playing background music
     */
    public void playBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            updateMusicVolume();
            backgroundMusicPlayer.play();
            System.out.println("Background music started");
        }
    }

    /**
     * Stop background music
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            System.out.println("Background music stopped");
        }
    }

    /**
     * Pause background music
     */
    public void pauseBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
            System.out.println("Background music paused");
        }
    }

    /**
     * Resume background music
     */
    public void resumeBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            updateMusicVolume();
            backgroundMusicPlayer.play();
            System.out.println("Background music resumed");
        }
    }

    /**
     * Play click sound effect
     */
    public void playClickSound() {
        if (clickSound != null && getEffectiveClickVolume() > 0) {
            clickSound.setVolume(getEffectiveClickVolume());
            clickSound.play();
        }
    }

    /**
     * Update the music player volume based on current settings
     */
    private void updateMusicVolume() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(getEffectiveMusicVolume());
        }
    }

    // ==================== VOLUME GETTERS/SETTERS ====================

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

    // ==================== MUTE GETTERS/SETTERS ====================

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

    // ==================== EFFECTIVE VOLUME CALCULATIONS ====================

    /**
     * Get the effective click sound volume (considering master volume and mute states)
     * Returns value between 0.0 and 1.0
     */
    public double getEffectiveClickVolume() {
        if (masterMuted || clickMuted) {
            return 0;
        }
        return (masterVolume / 100.0) * (clickVolume / 100.0);
    }

    /**
     * Get the effective music volume (considering master volume and mute states)
     * Returns value between 0.0 and 1.0
     */
    public double getEffectiveMusicVolume() {
        if (masterMuted || musicMuted) {
            return 0;
        }
        return (masterVolume / 100.0) * (musicVolume / 100.0);
    }

    private double clamp(double value) {
        return Math.max(0, Math.min(100, value));
    }

    /**
     * Cleanup resources when application closes
     */
    public void cleanup() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.dispose();
        }
    }
}