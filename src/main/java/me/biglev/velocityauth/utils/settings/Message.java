package me.biglev.velocityauth.utils.settings;

import java.util.ArrayList;

public class Message {

    private ArrayList<Registration> registration;
    private ArrayList<Login> login;
    private ArrayList<Premium> premium;
    private ArrayList<Title_Settings> title;
    private ArrayList<Error_Message> error_messages;

    public Registration getRegistration() {
        return registration.get(0);
    }

    public Premium getPremium() {
        return premium.get(0);
    }

    public Login getLogin() {
        return login.get(0);
    }

    public Title_Settings getTitle_settings() {
        return title.get(0);
    }

    public Error_Message getError_messages() {
        return error_messages.get(0);
    }

    @Override
    public String toString() {
        return "Message{" +
                "registration=" + registration +
                ", title_settings=" + title +
                ", error_messages=" + error_messages +
                '}';
    }

    public class Registration {
        private String register_request, command_usage, success;

        public String getRegister_request() {
            return register_request;
        }

        public String getCommand_usage() {
            return command_usage;
        }

        public String getSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "Registration{" +
                    "register_request='" + register_request + '\'' +
                    ", command_usage='" + command_usage + '\'' +
                    ", success='" + success + '\'' +
                    '}';
        }
    }

    public class Login {
        private String command_usage, wrong_password, success, login_request;

        public String getCommand_usage() {
            return command_usage;
        }

        public String getWrong_password() {
            return wrong_password;
        }

        public String getSuccess() {
            return success;
        }

        public String getLogin_request() {
            return login_request;
        }

        @Override
        public String toString() {
            return "Login{" +
                    "command_usage='" + command_usage + '\'' +
                    ", wrong_password='" + wrong_password + '\'' +
                    ", success='" + success + '\'' +
                    ", login_request='" + login_request + '\'' +
                    '}';
        }
    }

    public class Premium {
        private String already_premium, success;

        public String getAlready_premium() {
            return already_premium;
        }

        public String getSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "Premium{" +
                    "already_premium='" + already_premium + '\'' +
                    ", success='" + success + '\'' +
                    '}';
        }
    }

    public class Title_Settings {
        private ArrayList<Title_Register> register;
        private ArrayList<Title_Login> login;
        public ArrayList<Title_Register> getRegister() {
            return register;
        }

        public ArrayList<Title_Login> getLogin() {
            return login;
        }

        @Override
        public String toString() {
            return "Title_Settings{" +
                    ", regiter=" + register +
                    ", login=" + login +
                    '}';
        }
    }

    public class Title_Register {
        private String mainTitle, subTitle;

        public String getMainTitle() {
            return mainTitle;
        }

        public String getSubTitle() {
            return subTitle;
        }

        @Override
        public String toString() {
            return "Title_Register{" +
                    "mainTitle='" + mainTitle + '\'' +
                    ", subTitle='" + subTitle + '\'' +
                    '}';
        }
    }

    public class Title_Login {
        private String mainTitle, subTitle;

        public String getMainTitle() {
            return mainTitle;
        }

        public String getSubTitle() {
            return subTitle;
        }

        @Override
        public String toString() {
            return "Title_Login{" +
                    "mainTitle='" + mainTitle + '\'' +
                    ", subTitle='" + subTitle + '\'' +
                    '}';
        }
    }

    public class Error_Message {
        private String login_required, logged_in, name_length, password_length;

        public String getLogin_required() {
            return login_required;
        }

        public String getLogged_in() {
            return logged_in;
        }

        public String getName_length() {
            return name_length;
        }

        public String getPassword_length() {
            return password_length;
        }

        @Override
        public String toString() {
            return "Error_Message{" +
                    "login_required='" + login_required + '\'' +
                    ", logged_in='" + logged_in + '\'' +
                    ", name_length='" + name_length + '\'' +
                    ", password_length='" + password_length + '\'' +
                    '}';
        }
    }
}
