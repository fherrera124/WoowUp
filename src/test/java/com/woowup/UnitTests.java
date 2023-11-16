package com.woowup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.woowup.domain.Alert;
import com.woowup.domain.AlertTypeEnum;
import com.woowup.domain.Topic;
import com.woowup.domain.User;

class UnitTests {

    User userTest;

    Topic topicTest;

    // method executed before each test
    @BeforeEach
    public void init() {

        userTest = new User("Woow", "Up");
        topicTest = new Topic("art");

        new Alert(AlertTypeEnum.INFORMATIVE, topicTest, "");
    }

    @Test
    void subscriptionTest() {

        // the subscription
        topicTest.subscribe(userTest);

        // the topic has one alert
        assertEquals(1, topicTest.getAlerts().size());

        // the user has 0 alerts, because the alert (on init()
        // method) was emited BEFORE the subscription
        assertEquals(0, userTest.getAlerts().size());

        new Alert(AlertTypeEnum.INFORMATIVE, topicTest, "");

        // topic received the alert, as usual
        assertEquals(2, topicTest.getAlerts().size());
        // the user received his first alert from the topic,
        // since the alert was emited AFTER the subscription
        assertEquals(1, userTest.getAlerts().size());

    }

    @Test
    void expiredAlertsTest() throws InterruptedException {

        var alert = topicTest.getAlerts().get(0);
        assertNull(alert.getExpirationDate());

        // alert is active since has no expiration date
        assertEquals(1, topicTest.getAlerts().size());

        var now = LocalDateTime.now();

        // invalid expiration date (yesterday)
        assertThrows(IllegalArgumentException.class,
                () -> alert.setExpirationDate(now.minus(1, ChronoUnit.DAYS)));

        alert.setExpirationDate(now.plus(1, ChronoUnit.SECONDS));
        Thread.sleep(2000);
        // alert must be inactive after two second
        assertEquals(0, topicTest.getAlerts().size());

    }

    @Test
    void readAlertTest() {

        topicTest.subscribe(userTest);
        var alert = new Alert(AlertTypeEnum.INFORMATIVE, topicTest, "");
        assertEquals(1, userTest.getAlerts().size());

        userTest.markAlertAsRead(alert);
        // no alert pending to read
        assertEquals(0, userTest.getAlerts().size());

        // trying to mark the alert as read for the same user again throws
        // IllegalStateException
        assertThrows(IllegalStateException.class, () -> userTest.markAlertAsRead(alert));

    }

    @Test
    void alertNotRegisteredTest() {

        var alert = new Alert(AlertTypeEnum.URGENT, topicTest, "New art paintings have arrived at the museum");

        // the user is not subscribed to the topic
        assertThrows(IllegalArgumentException.class, () -> userTest.markAlertAsRead(alert));

        // the user is not subscribed to the topic
        assertThrows(IllegalArgumentException.class, () -> userTest.wasRead(alert));
    }

    @Test
    void alertTargetTest() {

        var topic = new Topic("");

        new Alert(AlertTypeEnum.INFORMATIVE, topic, "ALERT1");
        new Alert(AlertTypeEnum.INFORMATIVE, topic, userTest, "ALERT2");
        new Alert(AlertTypeEnum.URGENT, topic, "ALERT3");
        new Alert(AlertTypeEnum.INFORMATIVE, topic, userTest, "ALERT4");
        new Alert(AlertTypeEnum.URGENT, topic, "ALERT5");

        for (var alert : topic.getAlerts()) {
            switch (alert.getContent()) {
                case "ALERT2":
                case "ALERT4":
                    // the alert is targeted to an specific subscriber
                    assertTrue(alert.isTargeted());
                    break;
                case "ALERT1":
                case "ALERT3":
                case "ALERT5":
                    // the alert is targeted to all subscribers
                    assertFalse(alert.isTargeted());
                    break;
                default:
                    break;
            }
        }

    }

    @Test
    void alertTargetedOnlyTest() {

        var secondUserTest = new User("", "");

        topicTest.subscribe(userTest);
        topicTest.subscribe(secondUserTest);

        new Alert(null, topicTest, "");

        // both users has only alert active (the same alert)
        assertEquals(1, userTest.getAlerts().size());
        assertEquals(1, secondUserTest.getAlerts().size());

        // alert targeted to secondUserTest
        new Alert(null, topicTest, secondUserTest, "");

        // userTest was not notified by the topic, even though he is
        // subscribed to the topic and the alert belongs to that topic.
        // That's because the alert was targeted specifically to another user
        assertEquals(1, userTest.getAlerts().size());

        // secondUserTest was the only notified
        assertEquals(2, secondUserTest.getAlerts().size());
    }

    @Test
    void sortingTest() {

        // Dadas las siguientes alertas
        // Informativas y Urgentes que llegan en el siguiente orden: I1,I2,U1,I3,U2,I4
        // se ordenarán de la siguiente forma --> U2,U1,I1,I2,I3,I4

        var topic = new Topic("");

        new Alert(AlertTypeEnum.INFORMATIVE, topic, "I1");
        new Alert(AlertTypeEnum.INFORMATIVE, topic, "I2");
        new Alert(AlertTypeEnum.URGENT, topic, "U1");
        new Alert(AlertTypeEnum.INFORMATIVE, topic, "I3");
        new Alert(AlertTypeEnum.URGENT, topic, "U2");
        new Alert(AlertTypeEnum.INFORMATIVE, topic, "I4");

        var alerts = topic.getAlerts();

        assertEquals("U2", alerts.get(0).getContent());
        assertEquals("U1", alerts.get(1).getContent());
        assertEquals("I1", alerts.get(2).getContent());
        assertEquals("I2", alerts.get(3).getContent());
        assertEquals("I3", alerts.get(4).getContent());
        assertEquals("I4", alerts.get(5).getContent());

    }

}