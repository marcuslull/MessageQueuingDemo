package com.marcuslull.momdemo.service;

import com.marcuslull.momdemo.model.enums.Difficulty;
import com.marcuslull.momdemo.model.enums.Production;
import com.marcuslull.momdemo.model.enums.Rarity;
import com.marcuslull.momdemo.model.enums.TechLevel;
import com.marcuslull.momdemo.model.records.ResourceRecord;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;


@Service
public class RecordService {
    private final ResourceRecord waterRecord;
    private final ResourceRecord foodRecord;
    private final ResourceRecord workRecord;
    private final ResourceRecord educationRecord;
    private final ResourceRecord stoneRecord;
    private final ResourceRecord woodRecord;
    private final ResourceRecord energyRecord;

    // Descriptions provided by: https://bard.google.com/
    private final String abstractionDescription = "Abstraction layers civilization like a skyscraper, " +
            "lifting us from the ground of basic needs to soar among the clouds of innovation, " +
            "each level built on the invisible foundations of knowledge and trust.";
    private final String cropDescription = "Not castles, but crops, nourished lives: a whisper of survival etched in stalks, " +
            "sung in rustling leaves, a primal bond between hand and soil.";
    private final String dataDescription = "No longer just whispers on parchment, knowledge dances in shimmering streams " +
            "of data. Cogs turn, gears grind, minds alight with patterns gleaned from whispered numbers. Information, " +
            "the lifeblood of a burgeoning age, hums through wires, etched in silicon, shaping tomorrow from the whispers of today.";
    private final String educationDescription = "Education, the spark that ignites minds across ages. Whispers in caves, " +
            "tales by crackling fires, knowledge passed from hand to hand, a flickering flame against the darkness of ignorance. " +
            "From scribbling on clay to surfing on knowledge waves, each lesson leaves a constellation in the " +
            "mind's night sky, a whispered map guiding one's own journey. In every age, education whispers futures " +
            "into potential, a chorus of learning echoing through generations, its music shaping the world itself.";
    private final String energyDescription1 = "The sun's kiss on skin, the sweat-fueled hunt, flickering flames illuminating " +
            "a cave's embrace - primitive energy, raw and unyielding. Every step, every chase, a pulse echoing in " +
            "tired limbs, a flicker of warmth defying the night's chill. This is the primal dance of survival, " +
            "where energy is as fleeting as breath itself.";
    private final String energyDescription2 = "Wind whispers through blades, turning gears, muscles augmented by rushing " +
            "streams. Coal whispers secrets of heat, coaxed from the earth's embrace. Bellows sing, iron breathes fire, " +
            "forging steel and shaping dreams. This is the symphony of progress, where energy dances to the tune of " +
            "human ingenuity, pushing back the darkness, one clanging anvil at a time.";
    private final String energyDescription3 = "Invisible threads hum through wires, silent currents whispering power. " +
            "Coiled lightning sleeps in batteries, harnessed from the sun's caress. This is the ballet of the invisible, " +
            "where energy pirouettes through circuits, bends time and space, and whispers promises of boundless " +
            "possibility. The universe itself becomes a conductor, and humanity, its eager apprentice, in this grand " +
            "choreography of the cosmos.";
    private final String foodDescription = "Not plates, but palms, cradling hard-won berries, the earth's rough bounty. " +
            "Each bite, a testament to survival, a gruel of grit and grace, sung in the rustle of leaves, " +
            "the crackle of fire. Food, not feast, a primal song of need, where the hunt echoes in every swallow, " +
            "the sun's kiss lingers on every tongue.";
    private final String geneticMaterialDescription = "No longer captive to chance, life dances to the tune of a rewritten code. " +
            "Strands of shimmering nucleotides, whispered secrets of ancestral echoes, now hold the power to sculpt " +
            "destinies. Advanced minds, armed with stethoscopes of understanding, peer into the helix's heart, " +
            "coaxing forth symphonies of potential. Genetic material, once a whispered oracle, now sings a vibrant " +
            "aria of possibility, where futures bloom from the delicate touch of knowledge.";
    private final String mineralDescription = "No longer just pebbles beneath bare feet, the earth whispers secrets of utility " +
            "in glittering tongues. Copper, once a trinket, now dances in wires, whispering of light and voices " +
            "carried on the wind. Iron, the mountain's blood, whispers strength in blades and plows, shaping futures " +
            "from hard-won fields. Minerals, whispers translated into tools, whispering a civilization's murmur: " +
            "malachite's green whispers adornment, turquoise's blue whispers trade, and lapis lazuli's starry hues " +
            "whisper whispers of the heavens.";
    private final String quantumMaterialDescription = "Beyond the whisper of atoms, where logic bends and reality shimmers, " +
            "quantum materials sing unheard melodies. Electrons dance in impossible waltzes, entangled in secrets " +
            "too fine for language. Superposition whispers possibilities, materials both here and not, whispering worlds unseen. " +
            "Condensed stardust, these materials bend light, defy limits, and whisper futures whispered only in " +
            "dreams: teleportation's embrace, minds melded across the void, reality reshaped by a whisper's touch. " +
            "The universe, an open book, awaits the pen of quantum materials, its ink, the whispers of the impossible.";
    private final String rareEarthElementDescription = "Not magic, but marvels, these rare whispers of the earth. Cerium, once a curiosity, " +
            "now hums in batteries, whispering power without flame. Neodymium, the moon's ghost, dances in magnets, " +
            "shaping futures where wind whispers electricity. Scandium, the sun's echo, strengthens bones, " +
            "whispers bridges reaching towards the sky. Rare earths, no longer whispers in dusty tomes, but the " +
            "chorus of progress, etched in glowing screens and silent engines, whispering a middle-aged society's " +
            "quiet awe: the invisible magic woven into the fabric of their daily lives.";
    private final String spaceExplorationDescription = "Not rockets to conquer, but telescopes to touch. City lights dim to " +
            "starlight's whispers, a near-future society tiptoeing into the cosmic sea. Rovers scrawl Martian mysteries, " +
            "whispering secrets from red sands. The cosmos, a glimpsed ocean, paints futures in stardust whispers.";
    private final String stoneDescription = "Before walls, before tools, stone hums a rough song. Flint whispers fire, " +
            "chipped from mountains' bones. Obsidian gleams, a black tear of earth, whispering blades for hunt and " +
            "hunger's hush. Stone, not shelter, but survival's whisper, etched in hands and whispered stories, " +
            "where lives carve against the world's hard skin.";
    private final String syntheticMaterialDescription = "No longer clay from mountain's breath, but molecules whispered into being. " +
            "Polymer threads spun from air, woven light as fabric, whispering comfort with no cotton's touch. " +
            "Diamond's ghost, crafted in labs, whispers strength without the earth's groan. Synthetic materials, " +
            "the universe reshaped, whispered futures from stardust's dance in beakers, where nature bows to the human mind's song.";
    private final String timberDescription = "No longer whispers, but felled giants sing a song of progress. " +
            "Ax and saw etch dreams in fallen trunks, timbers rising where branches swayed. Houses whisper of permanence, " +
            "bridges defy rushing streams, a young society carving its future from the forest's whispered strength. ";
    private final String waterDescription = "Mirror of the sky, cradle of life, water whispers from every spring. " +
            "Primitive hands cup its coolness, quench thirst, whisper prayers to unseen spirits. In its flow, " +
            "a society sees its own, ever-changing, reflecting the moon's dance and the sun's kiss. ";
    private final String woodDescription = "Not walls, but branches reach, arms of the forest whispering shelter. " +
            "Fire's cradle, spear's spine, wood sings with the wind's song, whispers warmth against the night's chill. " +
            "Each splinter, a whispered memory of sun-dappled leaves, a primitive society cradled in nature's embrace. ";
    private final String workDescription = "Whispers in the wind, sweat on brows, dreams in calloused hands. " +
            "Work, the symphony of survival, a melody hummed in every age. Not just toil for bread, but a brushstroke " +
            "on the canvas of progress, a whispered song woven into the fabric of civilization. " +
            "From tilling soil to shaping stars, each labor adds a verse to the chorus, a testament to the human " +
            "spirit's tireless hum.";
    private final Map<String, Integer> waterRequirements = new HashMap<>();
    private final Map<String, Integer> foodRequirements = Map.ofEntries(entry("Water", 2));
    private final Map<String, Integer> workRequirements = Map.ofEntries(entry("Water", 2), entry("Food", 1));
    private final Map<String, Integer> educationRequirements = Map.ofEntries(entry("Water", 1), entry("Food", 1), entry("Work", 10));
    private final Map<String, Integer> stoneRequirements = Map.ofEntries(entry("Water", 3), entry("Food", 1), entry("Work", 3));
    private final Map<String, Integer> woodRequirements = Map.ofEntries(entry("Water", 2), entry("Food", 1), entry("Work", 2));
    private final Map<String, Integer> energyRequirements = Map.ofEntries(entry("Work", 1), entry("Wood", 1));


    public RecordService() {
        this.waterRecord = new ResourceRecord("Water", waterDescription, TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.FAST, Difficulty.EASY, waterRequirements);
        this.foodRecord = new ResourceRecord("Food", foodDescription, TechLevel.TECH_LEVEL_1, Rarity.UNCOMMON, Production.MEDIUM, Difficulty.MEDIUM, foodRequirements);
        this.workRecord = new ResourceRecord("Work", workDescription, TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.FAST, Difficulty.MEDIUM, workRequirements);
        this.educationRecord = new ResourceRecord("Education", educationDescription, TechLevel.TECH_LEVEL_1, Rarity.RARE, Production.SLOW, Difficulty.HARD, educationRequirements);
        this.stoneRecord = new ResourceRecord("Stone", stoneDescription, TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.MEDIUM, Difficulty.EASY, stoneRequirements);
        this.woodRecord = new ResourceRecord("Wood", woodDescription, TechLevel.TECH_LEVEL_1, Rarity.COMMON, Production.FAST, Difficulty.MEDIUM, woodRequirements);
        this.energyRecord = new ResourceRecord("Energy",energyDescription1, TechLevel.TECH_LEVEL_1, Rarity.RARE, Production.FAST, Difficulty.EASY, energyRequirements);
    }
    public ResourceRecord getRecord(String name) {
        switch (name.toLowerCase()) {
            case "water" -> { return waterRecord; }
            case "food" -> { return foodRecord; }
            case "work" -> { return workRecord; }
            case "education" -> { return educationRecord; }
            case "stone" -> { return stoneRecord; }
            case "wood" -> { return woodRecord; }
            case "energy" -> { return energyRecord; }
            default -> { return null; }
        }
    }
}
