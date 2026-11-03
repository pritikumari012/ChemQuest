package com.chemquest.app;

import com.chemquest.data.LocalUserRepository;
import com.chemquest.data.UserRepository;
import com.chemquest.model.User;
import com.chemquest.service.ApparatusService;
import com.chemquest.service.AuthenticationService;
import com.chemquest.service.ExperimentService;
import com.chemquest.service.PeriodicTableService;
import com.chemquest.service.ProgressService;
import com.chemquest.service.QuizService;

/**
 * Holds what the whole app shares: the services and the currently logged-in user.
 * Later phases will add ProgressService, QuizService, etc. here.
 */
public class AppState {
    private final UserRepository userRepository = new LocalUserRepository();
    private final AuthenticationService authService = new AuthenticationService(userRepository);
    private final PeriodicTableService periodicTable = new PeriodicTableService();
    private final ExperimentService experimentService = new ExperimentService();
    private final ApparatusService apparatusService = new ApparatusService(experimentService);
    private final QuizService quizService = new QuizService();
    private final ProgressService progressService = new ProgressService();
    private User currentUser;

    public AuthenticationService auth() {
        return authService;
    }

    public PeriodicTableService periodicTable() {
        return periodicTable;
    }

    public ExperimentService experiments() {
        return experimentService;
    }

    public ApparatusService apparatus() {
        return apparatusService;
    }

    public QuizService quizzes() {
        return quizService;
    }

    public ProgressService progress() {
        return progressService;
    }

    public User currentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    /** Clears the active session. Saved data (accounts, later progress) stays on disk. */
    public void logout() {
        this.currentUser = null;
    }
}
