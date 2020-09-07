// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


public final class FindMeetingQuery {
  /**Return all possible time intervals for a meeting, according to its attendees' events.*/
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> meetings = findOptionalTimeForMeeting(events, request);
    return meetings;
  }

  /**Return all possible time intervals for a meeting, according to its attendees' events.*/
  private ArrayList<TimeRange> findOptionalTimeForMeeting(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> meetings = new ArrayList<TimeRange>();
    ArrayList<TimeRange> sortedTimeRanges = filterOutIrrelevantMeetings(events, request.getAttendees());
    int start = TimeRange.START_OF_DAY;
    int end;

    for (TimeRange eventTime : sortedTimeRanges) {
      end = eventTime.start();
        if (request.getDuration() <= end - start) {
          meetings.add(TimeRange.fromStartEnd(start, end, false));
        }
      start = Math.max(start, eventTime.end());
    }
    // Add the time interval between the end of the last meeting and the end of the day.
    end = TimeRange.END_OF_DAY;
    if (request.getDuration() <= end - start) {
      meetings.add(TimeRange.fromStartEnd(start, end, true));
    }

    return meetings;
  }

  /**Filter out the irrelevant meetings and return an ArrayList of all
  the time intervals which include at least one of the given attendees.*/
  private ArrayList<TimeRange> filterOutIrrelevantMeetings(Collection<Event> events, Collection<String> attendees) {
    ArrayList<TimeRange> sortedTimeRanges = new ArrayList<TimeRange>();
    for (Event event : events) {
      if (!Collections.disjoint(event.getAttendees(), attendees)) {
        sortedTimeRanges.add(event.getWhen());
      }
    }
    Collections.sort(sortedTimeRanges, TimeRange.ORDER_BY_START);
    return sortedTimeRanges;
  }
}
