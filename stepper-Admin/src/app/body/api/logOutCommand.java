package app.body.api;

import app.body.api.HttpStatusUpdate;

public interface logOutCommand  extends HttpStatusUpdate {
    void logout();
}
