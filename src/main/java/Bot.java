import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    private static String schedule = "`KA - 75`:\n" +
            "\n\n `Понеділок` \n2. ТПР(Лекція) \n3. Економіка\n4. Аналіз фінеконом даних" +
            "\n\n `Вівторок` \n1. ТПР(Практика) \n2. МШІ\n3. БИС/Моделювання систем" +
            "\n\n `Середа` \n2. МШІ(Лекція) \n3. Економіка\n5. Моделювання систем" +
            "\n\n `Четвер` \n16:00. Англ \n17:00. Управління проектами\n18:00. Управління проектами(Лекція)" +
            "\n\n `Субота` \n1. Java \n2. Java ";

    public static void main(String[] args) {
        ApiContextInitializer.init();
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        }catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        }catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {

        final Message message = update.getMessage();
        if(message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendStartMsg(message);
                    break;
                case "/week":
                    sendMsg(message, schedule);
            }
        }

    }

    private void sendStartMsg(Message message) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Type /week to get schedule");

        try {
            execute(sendMessage);
        }catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "cs_sche_bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    /*
    public void setButtons(SendMessage sendMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        final KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Київ"));
        keyboardFirstRow.add(new KeyboardButton("Чернігів"));
        keyboardFirstRow.add(new KeyboardButton("Черкаси"));

        keyboardRowList.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);


    }
     */
}
