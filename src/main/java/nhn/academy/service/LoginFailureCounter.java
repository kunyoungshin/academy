package nhn.academy.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginFailureCounter {
    private final Map<String, Integer> failureMap = new ConcurrentHashMap<>();

    public void increment(String username) {
        failureMap.merge(username, 1, Integer::sum);
    }

    public int getFailures(String username) {
        return failureMap.getOrDefault(username, 0);
    }

    public void reset(String username) {
        failureMap.remove(username);
    }
}
