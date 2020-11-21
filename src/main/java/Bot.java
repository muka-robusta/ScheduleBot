import db.impl.EventRepoImpl;
import db.impl.SubjectRepoImpl;
import model.Event;
import model.Subject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import res.WDay;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class Bot extends TelegramLongPollingBot {

    private static String schedule = "`KA - 75`:\n" +
            "\n\n `Понеділок` \n2. ТПР(Лекція) \n3. Економіка\n4. Аналіз фінеконом даних" +
            "\n\n `Вівторок` \n1. ТПР(Практика) \n2. МШІ\n3. БИС/Моделювання систем" +
            "\n\n `Середа` \n2. МШІ(Лекція) \n3. Економіка\n5. Моделювання систем" +
            "\n\n `Четвер` \n16:00. Англ \n17:00. Управління проектами\n18:00. Управління проектами(Лекція)" +
            "\n\n `Субота` \n1. Java \n2. Java ";

    public static void main(String[] args) {

        final EventRepoImpl eventRepo = new EventRepoImpl();
        final SubjectRepoImpl subjectRepo = new SubjectRepoImpl();


        //try {

            /*

            ekonomika.setName("Економіка");
            subjectRepo.update(ekonomika);
            */

            /*
            final Subject ekonomika = subjectRepo.getSubject(-2054175645);
            final Subject makar = subjectRepo.getSubject(-1300838839);
            final Subject mshi = subjectRepo.getSubject(-1093375606);
            final Subject tpr = subjectRepo.getSubject(-286108916);
            final Subject angl = subjectRepo.getSubject(272334518);
            final Subject afed = subjectRepo.getSubject(420610291);
            final Subject javaSubj = subjectRepo.getSubject(470482832);
            final Subject uprPro = subjectRepo.getSubject(654990889);
            */

            /*
            eventRepo.create(new Event(
                    UUID.randomUUID().hashCode(),
                    javaSubj,
                    Event.convertToGCalendar(8,30),
                    1,
                    0,
                    "лекція",
                    WDay.SAT

            ));

            eventRepo.create(new Event(
                    UUID.randomUUID().hashCode(),
                    javaSubj,
                    Event.convertToGCalendar(10,25),
                    2,
                    0,
                    "практика",
                    WDay.SAT
            ));

            eventRepo.create(new Event(
                    UUID.randomUUID().hashCode(),
                    uprPro,
                    Event.convertToGCalendar(18,00),
                    3,
                    0,
                    "лекція",
                    WDay.THR
                    ));

            eventRepo.create(new Event(
                    UUID.randomUUID().hashCode(),
                    bis,
                    Event.convertToGCalendar(16,10),
                    5,
                    1,
                    "практика",
                    WDay.WED
            ));
            */
/*
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


 */

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
                    break;
                case "/subjects":
                    showSubjects(message);
                    break;
                case "/enjoy":
                    showEvents(message);
                    break;
                case "/today":
                    sendDaySchedule(message);
            }
        }

    }

    private void sendDaySchedule(Message message) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.enableMarkdown(true);
        final EventRepoImpl eventRepo = new EventRepoImpl();

        final GregorianCalendar today = new GregorianCalendar();
        final WDay todayWeekDay = WDay.convertFromCalendar(today);
        try {
            final List<Event> eventsByDay = eventRepo.getEventsByDay(todayWeekDay);
            if (eventsByDay.size() == 0) sendMessage.setText("На сьогодні занять немає");
            else {
                final StringBuffer stringBuffer = new StringBuffer("`" + WDay.getUkrName(todayWeekDay) + "`: \n\n");
                eventsByDay
                        .stream()
                        .forEach(event -> {
                            final int queueFlag = event.getQueueFlag();
                            stringBuffer.append(String.valueOf(queueFlag) + ". " + event.getSubject().getName() + "\n");

                        });
                sendMessage.setText(stringBuffer.toString());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void showEvents(Message message) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        final EventRepoImpl eventRepo = new EventRepoImpl();
        final SubjectRepoImpl subjectRepo = new SubjectRepoImpl();

/*
        try {
            /*
            final Subject subjectCheck = new Subject(
                    UUID.randomUUID().hashCode(),
                    "Економіка",
                    "google.com",
                    "Рощина");

            subjectRepo.create(subjectCheck);


            final Subject subjectCheck = subjectRepo.getSubject(112058277);
            eventRepo.create(
                    new Event(
                            UUID.randomUUID().hashCode(),
                            subjectCheck,
                            new GregorianCalendar(
                                    2020,
                                    11,
                                    1,
                                    10,
                                    25
                            ),
                            2,
                            0,
                            "Практика"
                    )
            );





        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
*/

        StringBuffer finalString = new StringBuffer("Расписание КА - 75:\n");
        try {
            eventRepo.getEvents()
                    .stream()
                    .forEach(event -> finalString.append("`["+ event.getWeekDay() +"][" + event.getQueueFlag() + "] "
                            + event.getSubject().getName() + " - "
                            + event.getTime().get(Calendar.HOUR) + ":" + event.getTime().get(Calendar.MINUTE)
                            + "(" + event.getEventType()
                            + ")`\n"));
            finalString.append("\nTotal: " + eventRepo.getEvents().size());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println(finalString);
        sendMessage.setText(finalString.toString());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void showSubjects(Message message) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());

        final SubjectRepoImpl subjectRepo = new SubjectRepoImpl();

        /*
        try {
            subjectRepo.delete(new Subject(-692882318, "ТПРъ", "google.com", "Zaichenko Yu. P."));
            subjectRepo.delete(new Subject(-508969804, "БИС", "google.com", "Мухин В.Е."));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        */



        StringBuffer buffer = new StringBuffer("");
        try {
            subjectRepo.getSubjects()
                    .stream()
                    .forEach(subject -> buffer.append("`[" + subject.getId() + "] " + subject.getName() + " - " + subject.getTutorName() + "`\n"));
            buffer.append("\nTotal: " + subjectRepo.getSubjects().size() + "\n");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        sendMessage.setText(buffer.toString());

        try {
            execute(sendMessage);
        }catch(TelegramApiException e) {
            e.printStackTrace();
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
        return "1468923638:AAFxoid1aE9ikbSMpnpJ_8mS3oflDALVMN9";
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
