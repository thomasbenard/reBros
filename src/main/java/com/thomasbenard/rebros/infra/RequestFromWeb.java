package com.thomasbenard.rebros.infra;

import com.thomasbenard.rebros.Request;

class RequestFromWeb extends Request {

    private final String separator = ",";
    private final String whereClauseSeparator = ":";

    RequestFromWeb(String selectedFields, String whereClauses) {
        if (selectedFields != null) {
            String[] selectedFieldsInArray = selectedFields.split(separator);
            select(selectedFieldsInArray);
        }
        if (whereClauses != null) {
            String[] whereClausesInArray = whereClauses.split(separator);
            for (String whereClause : whereClausesInArray) {
                String[] whereClauseParameters = whereClause.split(whereClauseSeparator);
                where(whereClauseParameters[0], whereClauseParameters[1]);
            }
        }
    }

}
