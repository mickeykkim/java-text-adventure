/* An obstacle entity in an adventure game. It has a custom verb which it responds
to, e.g. "cut".  It has an item which the player must have in order to solve
the puzzle, e.g. a key.  It has a failure message printed out if the player
doesn't have the needed item, and a success message if the puzzle is solved.
When the puzzle is solved, some entities are added to the current place,
e.g. things or exits which are now revealed, or a replacement for the puzzle to
change its state. */

import java.util.*;
import java.io.*;

class Obstacle extends Entity {
   private String verb, failure, success;
   private List<Thing> needs;
   private Place revealedPlace;

   Obstacle(String name, String description, String examine) {
      super(name, description, examine);
      needs = new ArrayList<Thing>();
   }

   // After constructing the obstacle, set up its parameters.
   void action(String verb, Thing[] needs, Place revealedPlace,
      String failure, String success)
   {
      this.verb = verb;
      for (Thing x: needs) this.needs.add(x);
      this.revealedPlace = revealedPlace;
      this.failure = failure;
      this.success = success;
   }

   // Announce the obstacle when the player finds it
   void arrive(Place here, PrintStream out) {
      out.println(description);
   }

   // Respond to the triggering verb
   Place act(Place here, String verb, PrintStream out) {
      if (verb.toLowerCase().equals("examine")) {
         out.println(examine);
         return here;
      } else if (!verb.toLowerCase().equals(this.verb)) {
         out.println("Do what to the " + name + "?");
         return here;
      }
      for (Thing n: needs) {
         if (here.find(n.name) == null) {
            out.println(failure);
            return here;
         }
      }
      System.out.println(success);
      here.put(revealedPlace);
      here.remove(this.name);
      return here;
   }
}