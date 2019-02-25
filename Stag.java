import java.util.*;
import java.io.*;

class Stag {
   Scanner scanner;
   Place here;
   String verb, noun;
   PrintStream out;
   boolean hasWon;

   void run() {
      startText();
      setup();
      here.act(here, "go", out);
      while (hasWon == false) {
         read();
         if (noun.equals(here.name)) {
            out.println("You are already at the " + here.name + ".");
            continue;
         }
         Entity e = here.find(noun);
         if (e == null) {
               out.println("What " + noun + "?");
               continue;
         }
         here = e.act(here, verb, out);
         if (e.examine.equals("You win.")) hasWon = true;
      }
      endText();
      exit();
   }

   void startText() {
      System.out.println("Welcome to Lucidity, a simple text adventure game.");
      System.out.println("A distant explosion awakens you from a deep sleep.");
      System.out.println("You see smoke coming from a ship.");
      System.out.println("Gathering yourself, you stand to your feet.");
   }

   void endText() {
      System.out.println("Examining the button, you try pressing it.");
      System.out.println("An alarm clock awakens you from a deep sleep.");
      System.out.println("You see light coming from between your curtains.");
      System.out.println("Gathering yourself, you get out of bed.");
   }

   // Creates initial game objects and sets initial room
   void setup() {
      scanner = new Scanner(System.in);
      out = System.out;
      
      Place beach = new Place("beach",
         "You are on the beach.\n" +
         "There is a raft on the sand and a forest further inland.",
         "The beach is completely deserted. A cold wind blows."
      );
      Place raft = new Place("raft",
         "You climb on the raft.\n" +
         "You see the beach and a ship anchored nearby. Billowing smoke rises from its deck.",
         "The raft is barely hanging together, battered by storms."
      );
      Place ship = new Place("ship",
         "You climb aboard the ship.\n" +
         "In the middle of the ship's deck is a ladder leading to the hull. The raft is tied to the side of the deck.",
         "The deck smells of ash and blood. What happened here?"
      );
      Place hull = new Place("hull",
         "You decend into the ship's hull.\n" +
         "Light peers through the hole above leading to the deck.",
         "The hull is dark, and smouldering wood illumates a table."
      );
      Place forest = new Place("forest",
         "Trees surroud you as you step into the forest.\n" +
         "There is a clearing beyond a large bush. You see a lake and a beach in the distance.",
         "The density of trees makes traveling further impossible."
      );
      Place lake = new Place("lake",
         "You reach a small lake.\n" +
         "The water is so clear you can see to the bottom. Behind you is the forest.",
         "Forest surrounds the lake and you cannot go around."
      );
      Place bottom = new Place("lake bottom",
         "You swim to the bottom of the lake.\n" +
         "You should head back to the lake surface while you still have air.",
         "The lake bottom is clear and eerily almost empty."
      );
      Place clearing = new Place("clearing",
         "You come to a open clearing.\n" +
         "The forest is behind you. A lock guards a lonely hut.",
         "The clearing looks weathered yet well kept."
      );
      Place hut = new Place("hut",
         "You enter the hut.\n" +
         "The hut is empty except for a large chest in the corner. The clearing is behind you.",
         "The hut looks to be recently vacated."
      );
      Thing machete = new Thing("machete",
         "There is a machete leaning against the wall.",
         "The machete is well worn but still sharp.");
      Thing paper = new Thing("paper",
         "You see a piece of paper with a code.",
         "The paper is singed on the corners. It holds the code 9453.");
      Thing key = new Thing("key",
         "You get a key.",
         "The key is old and rusty.");
      Thing button = new Thing("button",
         "You get a button.",
         "You win.");
      Obstacle bush = new Obstacle("bush",
         "There is a bush here.",
         "Perhaps it could be cut?");
      Puzzle safe = new Puzzle("safe",
         "There is a safe here with a passcode lock.",
         "It could be opened with the correct code.");
      Obstacle lock = new Obstacle("lock",
         "There is a lock here.",
         "The lock has a key hole.");
      Puzzle chest = new Puzzle("chest",
         "There is a chest here.",
         "The chest is obstructed by thick vines, a lock, and a code input device.");
      bush.action("cut", new Thing[] {machete}, clearing,
         "You need something sharp.",
         "You clear the bush. The clearing can now be entered."
      );
      safe.action("open", new Thing[] {paper}, new Thing[] {key},
         "You need a code.",
         "You open the safe."
      );
      lock.action("open", new Thing[] {key}, hut,
         "You need a key.",
         "You open the lock. The hut can now be entered."
      );
      chest.action("open", new Thing[] {machete, paper, key}, new Thing[] {button},
         "You need some items to open the chest.",
         "You open the chest."
      );
      beach.links(raft, forest);
      raft.links(beach, ship);
      ship.links(hull, raft, machete);
      hull.links(ship, paper);
      forest.links(beach, lake, bush);
      lake.links(forest, bottom);
      bottom.links(lake, safe);
      clearing.links(forest, lock);
      hut.links(clearing, chest);
      here = beach;
   }

   // Reads input from player
   void read() {
      System.out.print("> ");
      String line = scanner.nextLine();
      if (line.contains(" ")) {
         String[] words = line.split(" ");
         verb = words[0];
         StringBuilder noun = new StringBuilder();
         for (int i = 1; i < words.length; i++) {
            noun.append(words[i]);
            if (i < words.length - 1) {
               noun.append(" ");
            }
         }
         this.noun = noun.toString();
         return;
      } else if (line.toLowerCase().equals("help")) {
         help();
      } else if (line.toLowerCase().equals("examine")) {
         examine();
      } else if (line.toLowerCase().equals("exit")) {
         exit();
      } else {
         System.out.println("Please input at least two words.\n" + 
                            "For a list of commands, input \"help\".");
      }
      read();
   }

   // Examine the current place and prints list of interactable objects
   void examine() {
      System.out.println(here.examine);
      System.out.println("Looking around you see:");
      for (Map.Entry<String,Entity> e : here.survey().entrySet()) {
         System.out.println("A " + e.getKey() + ".");
      }
   }

   void exit() {
      System.out.println("Thanks for playing!");
      System.exit(0);
   }

   void help() {
      System.out.println("Valid action list:");
      System.out.println("  examine");
      System.out.println("  examine <anything>");
      System.out.println("  go <somewhere>");
      System.out.println("  take <something>");
      System.out.println("  drop <something>");
      System.out.println("  open <something>");
      System.out.println("  exit");
   }

   public static void main(String[] args) {
      Stag program = new Stag();
      program.run();
   }
}