package com.example.freepress.web;

import java.time.Instant;

/**
 * The masthead: when this run went to press, when the office opened, the drift between the two,
 * and today's slogan.
 *
 * @param wentToPress  when the press room stamped this run
 * @param officeOpened when the front desk opened for this run
 * @param driftSeconds absolute gap, in seconds, between the two moments above
 * @param quoteOfTheDay the slogan drawn for this run
 */
public record PressStatus(Instant wentToPress, Instant officeOpened, long driftSeconds, String quoteOfTheDay) {
}
