String[] all = {"creaking_heart_active", "creaking_heart_active_horizontal", "creaking_heart_horizontal", "creaking_heart"};
String[] woods = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "crimson", "warped", "bamboo"};

void start() {
  for (String file : all) {
    for (String wood : woods) {
      String[] json = loadStrings("../" + file + ".json");
      json[3] = json[3].replace("minecraft:block/", "creaking-sap:block/" + wood + "_");
      json[4] = json[4].replace("minecraft:block/", "creaking-sap:block/" + wood + "_");
      saveStrings(wood + "_" + file + ".json", json);
    }
  }
  exit();
}
