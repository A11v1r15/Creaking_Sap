String[] creakingSides = {"creaking_heart", "creaking_heart_active"};
String[] creakingTops = {"creaking_heart_top", "creaking_heart_top_active"};
String[] logs = {"oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log", "dark_oak_log", "mangrove_log", "cherry_log", "crimson_stem", "warped_stem", "bamboo_block"};

void start() {
  for (String log : logs) {
    PImage side = loadImage("../" + log + ".png");
    for (String creakingSide : creakingSides) {
      PImage overlay = loadImage("../" + creakingSide + ".png");
      PImage result = side.copy();
      result.set(0, 4, overlay.get(0, 4, 16, 8));
      result.save(log.split("_")[0] + "_" + creakingSide + ".png");
    }
    PImage top = loadImage("../" + log + "_top.png");
    for (String creakingTop : creakingTops) {
      PImage overlay = loadImage("../" + creakingTop + ".png");
      PImage result = top.copy();
      result.set(1, 1, overlay.get(1, 1, 14, 14));
      result.save(log.split("_")[0] + "_" + creakingTop + ".png");
    }
  }
  exit();
}
