package se.mtm.speech.synthesis.webui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FilibusterStatus { // NOPMD
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private final String type;
    private final Date created;
    private final Date endOfLife;

    private FilibusterStatus(Builder builder) {
        this.type = builder.type;
        this.created = builder.created;
        this.endOfLife = builder.endOfLife;
    }

    public String getType() {
        return type;
    }

    public String getCreated() {
        return sdf.format(created);
    }

    public String getEndOfLife() {
        return sdf.format(endOfLife);
    }

    public static class Builder {
        private String type; // NOPMD
        private Date created; // NOPMD
        private Date endOfLife; // NOPMD

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder created(Date created) {
            this.created = created;
            return this;
        }

        public Builder endOfLife(Date endOfLife) {
            this.endOfLife = endOfLife;
            return this;
        }

        public FilibusterStatus build() {
            return new FilibusterStatus(this); // NOPMD
        }
    }
}
