package com.mycompany.clientside.controllers;

import com.mycompany.clientside.utils.SoundManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SoundSettingsController implements Initializable {

    @FXML private Pane backgroundPane;
    @FXML private VBox dialogCard;

    @FXML private CheckBox masterMuteCheckBox;
    @FXML private CheckBox clickMuteCheckBox;
    @FXML private CheckBox musicMuteCheckBox;

    @FXML private Slider masterVolumeSlider;
    @FXML private Slider clickVolumeSlider;
    @FXML private Slider musicVolumeSlider;

    @FXML private Label masterVolumeLabel;
    @FXML private Label clickVolumeLabel;
    @FXML private Label musicVolumeLabel;

    private StackPane rootPane;
    private Runnable onBackCallback;

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backgroundPane.setOnMouseClicked(e -> goBack());
        dialogCard.setOnMouseClicked(e -> e.consume());

        // Load saved settings
        loadSavedSettings();

        // Setup slider listeners
        setupSliderListeners();

        // Set checkboxes (checked = enabled/not muted)
        SoundManager sm = SoundManager.getInstance();
        masterMuteCheckBox.setSelected(!sm.isMasterMuted());
        clickMuteCheckBox.setSelected(!sm.isClickMuted());
        musicMuteCheckBox.setSelected(!sm.isMusicMuted());

        // Update slider disabled state based on mute
        masterVolumeSlider.setDisable(sm.isMasterMuted());
        clickVolumeSlider.setDisable(sm.isClickMuted());
        musicVolumeSlider.setDisable(sm.isMusicMuted());
    }

    private void loadSavedSettings() {
        SoundManager sm = SoundManager.getInstance();

        masterVolumeSlider.setValue(sm.getMasterVolume());
        clickVolumeSlider.setValue(sm.getClickVolume());
        musicVolumeSlider.setValue(sm.getMusicVolume());

        updateVolumeLabel(masterVolumeLabel, sm.getMasterVolume());
        updateVolumeLabel(clickVolumeLabel, sm.getClickVolume());
        updateVolumeLabel(musicVolumeLabel, sm.getMusicVolume());
    }

    private void setupSliderListeners() {
        SoundManager sm = SoundManager.getInstance();

        masterVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double value = newVal.doubleValue();
            updateVolumeLabel(masterVolumeLabel, value);
            sm.setMasterVolume(value);
        });

        clickVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double value = newVal.doubleValue();
            updateVolumeLabel(clickVolumeLabel, value);
            sm.setClickVolume(value);
        });

        // Play test click when slider is released
        clickVolumeSlider.setOnMouseReleased(e -> {
            sm.playClickSound();
        });

        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double value = newVal.doubleValue();
            updateVolumeLabel(musicVolumeLabel, value);
            sm.setMusicVolume(value);
        });
    }

    private void updateVolumeLabel(Label label, double value) {
        label.setText(String.format("%.0f%%", value));
    }

    @FXML
    private void onMasterMuteChanged() {
        boolean enabled = masterMuteCheckBox.isSelected();
        masterVolumeSlider.setDisable(!enabled);
        SoundManager.getInstance().setMasterMuted(!enabled);
    }

    @FXML
    private void onClickMuteChanged() {
        boolean enabled = clickMuteCheckBox.isSelected();
        clickVolumeSlider.setDisable(!enabled);
        SoundManager.getInstance().setClickMuted(!enabled);
        
        // Play test sound if enabled
        if (enabled) {
            SoundManager.getInstance().playClickSound();
        }
    }

    @FXML
    private void onMusicMuteChanged() {
        boolean enabled = musicMuteCheckBox.isSelected();
        musicVolumeSlider.setDisable(!enabled);
        SoundManager.getInstance().setMusicMuted(!enabled);
    }

    @FXML
    private void goBack() {
        // Play click sound
        SoundManager.getInstance().playClickSound();
        
        // Remove this dialog
        if (rootPane != null && rootPane.getChildren().size() > 1) {
            rootPane.getChildren().remove(rootPane.getChildren().size() - 1);
        }

        // Execute callback if set
        if (onBackCallback != null) {
            onBackCallback.run();
        }
    }
}