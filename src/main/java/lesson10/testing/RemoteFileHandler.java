package lesson10.testing;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RemoteFileHandler {

    private SettingsLoader settingsLoader;
    private ResponseRepository responseRepository;

    public RemoteFileHandler(SettingsLoader settingsLoader, ResponseRepository responseRepository) {
        this.settingsLoader = settingsLoader;
        this.responseRepository = responseRepository;
    }

    //TODO: Необходимо написать тесты, покрывающие функционал метода
    public void handleRequest() {
        try {
            Map<String, String> settings = settingsLoader.loadSettings();
            if (settings != null && !settings.isEmpty()) {
                RemoteFileReadWriter remoteFileHandler = new RemoteFileReadWriter();
                List<String> request = remoteFileHandler.readFileToList(settings);
                responseRepository.writeResult(request);
            }
        } catch (Exception e) {
            responseRepository.writeError(e.getMessage());
        }
    }

    //TODO: Необходимо написать тесты, покрывающие функционал метода
    public void handleResponse(List<String> response) {
        if (validate(response)) {
            if (responseRepository.writeResult(response)) {
                RemoteFileReadWriter remoteFileHandler = new RemoteFileReadWriter();
                remoteFileHandler.writeResponse();
            }
        } else {
            responseRepository.writeError("Ошибка валидации");
        }
    }

    //TODO: Необходимо написать тесты, покрывающие функционал метода
    public boolean handleError(List<String> response) {
        if (response == null || response.isEmpty()) {
            return false;
        }
        if (response.contains("error")) {
            throw new IllegalArgumentException("Данные содержат ошибку");
        }
        return true;
    }

    //Эмуляция сложной логики. При написании иестов реальный метод вызываться не должен
    private boolean validate(List<String> response) {
        Random random = new Random();
        return random.nextBoolean();
    }
}
