package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnhancedYesNoBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final String githubLink = "https://github.com/Yezomsdt";
    private final String telegramUsername = "DAAAYUM";
    private final String[] imageResponses = {"Епт", "Красевое", "Ух ты!", "Кринж", "Убей себя"};
    private final String[] textResponses = {"Да", "Нет", "Может быть ¯\\_(ツ)_/¯"};

    public EnhancedYesNoBot(String botUsername, String botToken) {
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
        try {
            if (update.hasMessage()) {
                if (update.getMessage().hasText()) {
                    handleTextMessage(update);
                } else if (update.getMessage().hasPhoto()) {
                    handleImageMessage(update);
                }
            } else if (update.hasCallbackQuery()) {
                handleCallbackQuery(update.getCallbackQuery());
            }
        } catch (TelegramApiException e) {
            handleError(e, update);
        }
    }

    private void handleTextMessage(Update update) throws TelegramApiException {
        String messageText = update.getMessage().getText().toLowerCase();
        long chatId = update.getMessage().getChatId();

        if (messageText.startsWith("/start")) {
            sendMessageWithButtons(chatId);
            return;
        }
    
        if (messageText.endsWith("?")) {
            String response = getRandomResponse();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(response);
            execute(message);
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Добавь в конце вопрос, если хочешь что-то узнать.");
            execute(message);
        }
    }

    private String getRandomResponse() {
        Random random = new Random();
        return textResponses[random.nextInt(textResponses.length)];
    }

    private void handleImageMessage(Update update) throws TelegramApiException {
        long chatId = update.getMessage().getChatId();
        sendRandomImageResponse(chatId);
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId(); // Изменено на int

        if (callbackData.equals("github")) {
            String response = "GitHub: " + githubLink + "\nTelegram: @" + telegramUsername;
            EditMessageText editMessage = new EditMessageText();
            editMessage.setChatId(chatId);
            editMessage.setMessageId(messageId);
            editMessage.setText(response);
            execute(editMessage);
        }
    }

    private void sendMessageWithButtons(long chatId) throws TelegramApiException {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton(); // Создаем кнопку
        button.setText("Инфа о создателе");
        button.setCallbackData("github");
        rowInline.add(button);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбирай осторожно:");
        message.setReplyMarkup(markupInline);
        execute(message);
    }
    
    private void sendRandomImageResponse(long chatId) throws TelegramApiException {
        Random random = new Random();
        String response = imageResponses[random.nextInt(imageResponses.length)];
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(response);
        execute(message);
    }

    private void handleError(TelegramApiException e, Update update) {
        System.err.println("Ошибка Telegram API: " + e.getMessage());
    }

    public static void main(String[] args) {
        String botUsername = "ToosieSlideBot";
        String botToken = "";
        EnhancedYesNoBot bot = new EnhancedYesNoBot(botUsername, botToken);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
