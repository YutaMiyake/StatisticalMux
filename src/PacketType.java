// PacketType is a enum type to specify the type for a packet
// among three different choices: AUDIO, VIDEO, and TEXT

public enum PacketType{
    AUDIO(0), VIDEO(1), TEXT(2);
    private final int value;

    private PacketType(int value){
    // constructor
        this.value = value;
    }

    public int getValue() {
    // returns the value
        return value;
    }
  };