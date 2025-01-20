package com.deusto.strava_client.proxy;

import com.deusto.strava_client.data.*;

import java.time.LocalDate;
import java.util.List;

public interface IServiceProxy {

    /** Authentication Methods **/
    String login(Credentials credentials);

    boolean register(User user);

    User getUserInfo(String token);

    /** Challenge Methods **/
    List<Challenge> getActiveChallenges(String token);

    List<Challenge> getMyActiveChallenges(String token);

    void createChallenge(String token, Challenge challenge);

    /** Session Methods **/
    List<TrainingSession> getSessions(String token, LocalDate startDate, LocalDate endDate);

    void createTrainingSession(String token, TrainingSession session);

    void joinChallenge(String token, String challengeId);

    String sendMessageToGPT(String message);
}
