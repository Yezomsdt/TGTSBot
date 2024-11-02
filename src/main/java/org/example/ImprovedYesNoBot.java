package org.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.util.Random;
import java.util.List;
import java.util.Arrays;

public class ImprovedYesNoBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final String[] imageResponses = {"Ахуенный запил!!", "Вахуи", "Какой-то щит", "Заставляет задуматься", "Похоже на тебя"};

    public ImprovedYesNoBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                handleTextMessage(update);
            } else if (update.getMessage().hasPhoto()) {
                handleImageMessage(update);
            }
        }
    }

    private void handleTextMessage(Update update) {
        String messageText = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        String response = getRandomAnswer();
        sendMessage(chatId, response);
    }

    private void handleImageMessage(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String response = getRandomImageResponse();
        sendMessage(chatId, response);
    }


    private void handleError(TelegramApiException e, Update update) {
        System.err.println("Error processing update: " + update.toString() + "\nError message: " + e.getMessage());
        // Можно добавить логирование или отправку уведомления администратору
    }

    private String getRandomAnswer() {
        Random random = new Random();
        return random.nextBoolean() ? "Да" : "Нет";
    }

    private String getRandomImageResponse() {
        Random random = new Random();
        return imageResponses[random.nextInt(imageResponses.length)];
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            handleError(e, null); // Обработка ошибки при отправке сообщения
        }
    }

    public static void main(String[] args) {
        String botUsername = "ToosieSlideBot";
        String botToken = "7014038472:AAFnKWIel-vrCMuOk_bW2x-YF3_m5tZ1eAI";
        ImprovedYesNoBot bot = new ImprovedYesNoBot(botUsername, botToken);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}