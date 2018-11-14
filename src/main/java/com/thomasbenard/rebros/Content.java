package com.thomasbenard.rebros;

import java.util.Optional;

public interface Content {
    Optional<Match> get(String id);
}
