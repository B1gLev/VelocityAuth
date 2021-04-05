package me.biglev.velocityauth.utils.settings;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    private List<String> authServers;
    private List<String> commands;
    private ArrayList<Database> database;
    private ArrayList<Security> security;
    private ArrayList<Restrictions> restrictions;

    public List<String> getAuthServers() {
        return authServers;
    }

    public List<String> getCommands() {
        return commands;
    }

    public Database getDatabase() {
        return database.get(0);
    }

    public Security getSecurity() {
        return security.get(0);
    }

    public Restrictions getRestrictions() {
        return restrictions.get(0);
    }

    @Override
    public String toString() {
        return "Settings{" +
                "authServers=" + authServers +
                ", commands=" + commands +
                ", database=" + database +
                ", security=" + security +
                ", restrictions=" + restrictions +
                '}';
    }

    public class Database {
        private String host, username, password, database;
        private int port;
        private boolean useSSL;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean isUseSSL() {
            return useSSL;
        }

        public void setUseSSL(boolean useSSL) {
            this.useSSL = useSSL;
        }

        @Override
        public String toString() {
            return "Database{" +
                    "host='" + host + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", database='" + database + '\'' +
                    ", port=" + port +
                    ", useSSL=" + useSSL +
                    '}';
        }
    }

    public class Security {
        private int minPasswordLength, passwordMaxLength, bcryptRounds;
        private String passwordHash;

        public int getMinPasswordLength() {
            return minPasswordLength;
        }

        public int getPasswordMaxLength() {
            return passwordMaxLength;
        }

        public int getBcryptRounds() {
            return bcryptRounds;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        @Override
        public String toString() {
            return "Security{" +
                    "minPasswordLength=" + minPasswordLength +
                    ", passwordMaxLength=" + passwordMaxLength +
                    ", bcryptRounds=" + bcryptRounds +
                    ", passwordHash='" + passwordHash + '\'' +
                    '}';
        }
    }

    public class Restrictions {
        private int maxRegPerIp, minNicknameLength, maxNicknameLength;
        private boolean kickOnWrongPassword;

        public int getMaxRegPerIp() {
            return maxRegPerIp;
        }

        public void setMaxRegPerIp(int maxRegPerIp) {
            this.maxRegPerIp = maxRegPerIp;
        }

        public int getMinNicknameLength() {
            return minNicknameLength;
        }

        public void setMinNicknameLength(int minNicknameLength) {
            this.minNicknameLength = minNicknameLength;
        }

        public int getMaxNicknameLength() {
            return maxNicknameLength;
        }

        public void setMaxNicknameLength(int maxNicknameLength) {
            this.maxNicknameLength = maxNicknameLength;
        }

        public boolean isKickOnWrongPassword() {
            return kickOnWrongPassword;
        }

        public void setKickOnWrongPassword(boolean kickOnWrongPassword) {
            this.kickOnWrongPassword = kickOnWrongPassword;
        }

        @Override
        public String toString() {
            return "Restrictions{" +
                    "maxRegPerIp=" + maxRegPerIp +
                    ", minNicknameLength=" + minNicknameLength +
                    ", maxNicknameLength=" + maxNicknameLength +
                    ", kickOnWrongPassword=" + kickOnWrongPassword +
                    '}';
        }
    }
}
