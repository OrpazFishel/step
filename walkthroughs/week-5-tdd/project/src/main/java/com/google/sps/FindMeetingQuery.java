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
  /**
   * Return all possible time intervals for a meeting, taking into account the optional attendees, 
   * if it's possible, otherwise return all the possible time intervals without them.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> meetingsWithOptional = findTimeForMeeting(events, allAttendees(request), request.getDuration());
    if (!meetingsWithOptional.isEmpty()) {
        return meetingsWithOptional;
    }
    return findTimeForMeeting(events, mandatoryAttendees(request), request.getDuration());
  }

  /** Return all the attendees of the meeting request, both mandatory and optionals. */
  private Collection<String> allAttendees(MeetingRequest request) {
    Collection<String> allAttendees = new HashSet<String>();
    allAttendees.addAll(request.getAttendees());
    allAttendees.addAll(request.getOptionalAttendees());
    return allAttendees;
  }

  /** Return all the mandatory attendees of the meeting request. */
  private Collection<String> mandatoryAttendees(MeetingRequest request) {
    return request.getAttendees();
  }

  /** Return all possible time intervals for a meeting, according to its attendees' events. */
  private ArrayList<TimeRange> findTimeForMeeting(Collection<Event> events, Collection<String> attendees, long requestDuration) {
    ArrayList<TimeRange> sortedTimeWindows = filterOutIrrelevantMeetings(events, attendees);
    
    ArrayList<TimeRange> optionalTimeWindows = new ArrayList<TimeRange>();
    int start = TimeRange.START_OF_DAY;
    int end;

    for (TimeRange eventTime : sortedTimeWindows) {
      end = eventTime.start();
      if (TimeRange.fromStartEnd(start, end, false).duration() >= requestDuration) {
        optionalTimeWindows.add(TimeRange.fromStartEnd(start, end, false));
      }
      start = Math.max(start, eventTime.end());
    }
    // Add the time interval between the end of the last meeting and the end of the day.
    end = TimeRange.END_OF_DAY;
    if (TimeRange.fromStartEnd(start, end, true).duration() >= requestDuration) {
      optionalTimeWindows.add(TimeRange.fromStartEnd(start, end, true));
    }

    return optionalTimeWindows;
  }

  /**
   * Filter out the irrelevant meetings and return an ArrayList of all
   * the time intervals which include at least one of the given attendees.
   */
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
