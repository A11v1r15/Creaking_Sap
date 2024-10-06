String file = "creaking_heart";
String[] woods = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "crimson", "warped", "bamboo"};

void start() {
  for (String wood : woods) {
    String[] json = loadStrings("../" + file + ".json");
    json[1] = json[1].replace("minecraft:block/", "creaking-sap:block/" + wood + "_");
    saveStrings(wood + "_" + file + ".json", json);
  }
  exit();
}
