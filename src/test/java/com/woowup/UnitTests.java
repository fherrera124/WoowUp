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
import org.junit.jupiter.api.function.Executable;

import com.woowup.domain.InformativeAlert;
import com.woowup.domain.Topic;
import com.woowup.domain.UrgentAlert;
import com.woowup.domain.User;

class UnitTests {

    User userTest;

    Topic topicTest;

    // method executed before each test
    @BeforeEach
    public void init() {

        userTest = new User("Woow", "Up");
        topicTest = new Topic("art");

        InformativeAlert.builder().topic(topicTest).content("").build();
    }

    @Test
    void subscriptionTest() {

        // the subscription
        userTest.subscribeToTopic(topicTest);

        // the topic has one alert
        assertEquals(1, topicTest.getAlerts().size());

        // the user has 0 alerts, because the alert (on init()
        // method) was emited BEFORE the subscription
        assertEquals(0, userTest.getAlerts().size());

        InformativeAlert.builder().topic(topicTest).content("").build();

        // topic received the alert, as expected
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
        // alert must be inactive after two seconds
        assertEquals(0, topicTest.getAlerts().size());

    }

    @Test
    void readAlertTest() {

        userTest.subscribeToTopic(topicTest);

        var alert = InformativeAlert.builder().topic(topicTest).content("").build();

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

        var alert = UrgentAlert.builder().topic(topicTest).content("New art paintings have arrived at the museum")
                .build();

        // the user is not subscribed to the topic
        assertThrows(IllegalArgumentException.class, () -> userTest.markAlertAsRead(alert));

        // the user is not subscribed to the topic
        assertThrows(IllegalArgumentException.class, () -> userTest.wasRead(alert));
    }

    @Test
    void validateSubscriptionBeforeTargetedAlertTest() throws Throwable {

        var topic = new Topic("");

        Executable lambda = () -> InformativeAlert.builder().topic(topic).targetedUser(userTest).content("").build();

        // user cannot receive the targeted alert,
        // since is not subscribed to the topic
        assertThrows(IllegalArgumentException.class, lambda);

        userTest.subscribeToTopic(topic);
        // now the user can receive the targeted alert
        lambda.execute();

    }

    @Test
    void alertTargetTest() {

        var topic = new Topic("");
        userTest.subscribeToTopic(topic);

        InformativeAlert.builder().topic(topic).content("ALERT1").build();
        InformativeAlert.builder().topic(topic).targetedUser(userTest).content("ALERT2").build();
        UrgentAlert.builder().topic(topic).content("ALERT3").build();
        InformativeAlert.builder().topic(topic).targetedUser(userTest).content("ALERT4").build();
        UrgentAlert.builder().topic(topic).content("ALERT5").build();

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

        userTest.subscribeToTopic(topicTest);
        secondUserTest.subscribeToTopic(topicTest);

        InformativeAlert.builder().topic(topicTest).content("").build();

        // both users have only one alert active (the same alert)
        assertEquals(1, userTest.getAlerts().size());
        assertEquals(1, secondUserTest.getAlerts().size());

        // alert targeted to secondUserTest
        InformativeAlert.builder().topic(topicTest).targetedUser(secondUserTest).content("").build();

        // userTest was not notified by the topic, even though he is
        // subscribed to the topic and the alert belongs to that topic.
        // That's because the alert was targeted specifically to another user
        assertEquals(1, userTest.getAlerts().size());

        // secondUserTest was the only notified
        assertEquals(2, secondUserTest.getAlerts().size());
    }

    @Test
    void sortingTest() {
        /*
         * Given the following Informative and Urgent alerts that arrive in the
         * following order: I1,I2,U1,I3,U2,I4. They will be ordered as follows:
         * U2,U1,I1,I2,I3,I4
         */

        var topic = new Topic("");

        InformativeAlert.builder().topic(topic).content("I1").build();
        InformativeAlert.builder().topic(topic).content("I2").build();
        UrgentAlert.builder().topic(topic).content("U1").build();
        InformativeAlert.builder().topic(topic).content("I3").build();
        UrgentAlert.builder().topic(topic).content("U2").build();
        InformativeAlert.builder().topic(topic).content("I4").build();

        var alerts = topic.getAlerts();

        assertEquals("U2", alerts.get(0).getContent());
        assertEquals("U1", alerts.get(1).getContent());
        assertEquals("I1", alerts.get(2).getContent());
        assertEquals("I2", alerts.get(3).getContent());
        assertEquals("I3", alerts.get(4).getContent());
        assertEquals("I4", alerts.get(5).getContent());

    }

}