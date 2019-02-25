/* A place in a STAG adventure game.  A place has a collection of links to
entities, including things, puzzles and exits to other places. */

import java.io.*;
import java.util.*;

class Place extends Entity {
   private Map<String,Entity> links;

   Place(String name, String description, String examine) {
      super(name, description, examine);
   }

   // Add the links to things and other places, after construction
   void links(Entity... links) {
      if (this.links != null) throw new Error("Can only set links once.");
      this.links = new HashMap<String,Entity>();
      for (Entity e : links) this.links.put(e.name, e);
   }

   // Find an entity linked to this place
   Entity find(String name) {
      return links.get(name);
   }

   // Take a thing from this place
   Thing get(String name) {
      Thing x = (Thing) links.get(name);
      links.remove(name);
      return x;
   }

   // Add or replace a linked entity
   void put(Entity x) {
      links.put(x.name, x);
   }

   // Return a deep copy of the current Place's links
   Map<String,Entity> survey() {
      Map<String,Entity> deepCopyLinks = new HashMap<String,Entity>();
      for (Map.Entry<String,Entity> entry : links.entrySet()) {
         deepCopyLinks.put(entry.getKey(), entry.getValue());
      }
      return deepCopyLinks;
   }

   // Return a deep copy of the current Place
   Place copy() {
      Place deepCopyPlace = new Place(this.name, this.description, this.examine);
      deepCopyPlace.links = survey();
      return deepCopyPlace;
   }

   // Go to this place
   Place act(Place from, String verb, PrintStream out) {
      if (verb.toLowerCase().equals("examine")) {
         out.println(examine);
         return from;
      } else if (!verb.toLowerCase().equals("go")) {
         out.println("You can't do that to a place.");
         return from;
      }
      Place to = this;
      Place tempCopy = copy();
      for (Entity e: from.links.values()) e.move(tempCopy, to, out);
      out.println(description);
      for (Entity e: links.values()) e.arrive(to, out);
      return to;
   }
}
