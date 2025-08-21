package griffith;

import java.util.*;

//Kayky Gueno 3061913

public class cardgame {

    // Global variables for statistics and language settings
    static int gamesPlayed = 0;
    static int gamesWon = 0;
    static int gamesLost = 0;
    static int playerCredits = 100; // Starting credits
    static String language = "EN"; // Default language: English

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            showMainMenu();
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "1": // Start Game
                    playBlackjack(scanner);
                    break;
                case "2": // View Statistics
                    showStatistics();
                    break;
                case "3": // Change Language
                    changeLanguage(scanner);
                    break;
                case "4": // Quit
                    System.out.println(getMessage("exit_message"));
                    exit = true;
                    break;
                default:
                    System.out.println(getMessage("invalid_choice"));
            }
        }

        scanner.close();
    }

    // Displays the main menu
    public static void showMainMenu() {
        System.out.println("\n==============================");
        System.out.println(getMessage("welcome"));
        System.out.println("==============================");
        System.out.println("1. " + getMessage("start_game"));
        System.out.println("2. " + getMessage("view_stats"));
        System.out.println("3. " + getMessage("change_lang"));
        System.out.println("4. " + getMessage("quit"));
        System.out.println("==============================");
        System.out.print(getMessage("menu_prompt"));
    }

    // Plays the game of Blackjack
    public static void playBlackjack(Scanner scanner) {
        if (playerCredits <= 0) {
            System.out.println(getMessage("no_credits"));
            return;
        }
        
     // Initialize the deck and shuffle it to randomize the card order
        List<String> deck = initializeDeck();
        Collections.shuffle(deck);

     // Prompt the player to place their bet
        System.out.println(getMessage("place_bet"));
        int bet = 0;
        
     // Loop to ensure the player enters a valid bet amount
        while (true) {
            try {
                bet = Integer.parseInt(scanner.nextLine());
                
             // Check if the bet exceeds the player's available credits
                if (bet > playerCredits) {
                    System.out.println(getMessage("insufficient_credits"));
                    
                 // Check if the bet is less than or equal to zero (invalid bet)
                } else if (bet <= 0) {
                    System.out.println(getMessage("invalid_bet"));
                 // If the bet is valid, break out of the loop
                } else {
                    break;
                }
                
             // Handle non-integer inputs
            } catch (NumberFormatException e) {
                System.out.println(getMessage("invalid_bet"));
            }
        }
     // Initialize the hands for the player and the dealer
        List<String> playerHand = new ArrayList<>();
        List<String> dealerHand = new ArrayList<>();
        
     // Deal two cards to the player and two cards to the dealer
        playerHand.add(deck.remove(0));// Remove the top card from the deck and add it to the player's hand
        playerHand.add(deck.remove(0));
        dealerHand.add(deck.remove(0));// Remove the top card from the deck and add it to the dealer's hand
        dealerHand.add(deck.remove(0));

        System.out.println(getMessage("your_hand") + formatHand(playerHand) + " (Total: " + calculateBestValue(playerHand) + ")");
        System.out.println(getMessage("dealer_card") + dealerHand.get(0) + "\n");

        boolean playerTurn = true; // Variable to track if it's still the player's turn
        while (playerTurn) { // Player's turn loop
            int playerValue = calculateBestValue(playerHand);

            if (playerValue > 21) {
                System.out.println(visualBust() + getMessage("busted") + playerValue + ".");
                gamesPlayed++; // Increment games played counter
                gamesLost++; // Increment games lost counter
                playerCredits -= bet; // Increment games lost counter
                return;
            } else if (playerValue == 21) {
                System.out.println(visualWin() + getMessage("blackjack"));
                gamesPlayed++;
                gamesWon++;
                playerCredits += bet;
                return;
            }
         // Prompt the player to choose between "Hit" (draw a card) or "Stand" (end their turn)
            System.out.println(getMessage("your_total") + playerValue);
            System.out.println(getMessage("hit_stand"));
            String choice = scanner.nextLine().toUpperCase();
         // Read the player's choice for comparison
            if (choice.equals("1")) {
                playerHand.add(deck.remove(0));
                System.out.println(getMessage("you_drew") + playerHand.get(playerHand.size() - 1));
                System.out.println(getMessage("your_hand") + formatHand(playerHand) + " (Total: " + calculateBestValue(playerHand) + ")");
            } else if (choice.equals("2")) {
                playerTurn = false;
            } else { // Handle invalid input
                System.out.println(getMessage("invalid_choice"));
            }
        }
     // Dealer's turn: dealer must draw cards until their total is at least 17
        System.out.println(getMessage("dealer_turn"));
        while (calculateBestValue(dealerHand) < 17) {
            System.out.println(getMessage("dealer_hits"));
            dealerHand.add(deck.remove(0));
            System.out.println(getMessage("dealer_drew") + dealerHand.get(dealerHand.size() - 1));
        }
     // Determine and display the final result
        int dealerValue = calculateBestValue(dealerHand);
        System.out.println(getMessage("dealer_total") + dealerValue);

        int playerValue = calculateBestValue(playerHand);
        if (dealerValue > 21 || playerValue > dealerValue) {
        	// Player wins if dealer busts or player's total is higher
            System.out.println(visualWin() + getMessage("you_win"));
            gamesPlayed++;
            gamesWon++;
            playerCredits += bet;
        } else if (playerValue == dealerValue) { //If player draw 
            System.out.println(visualTie() + getMessage("tie"));
            gamesPlayed++;
        } else {
            System.out.println(visualLose() + getMessage("dealer_wins")); // If player loses
            gamesPlayed++;
            gamesLost++;
            playerCredits -= bet;
        }
    }

    // Shows statistics
    public static void showStatistics() {
        System.out.println("\n" + getMessage("stats_title"));
        System.out.println("==============================");
        System.out.println(getMessage("games_played") + gamesPlayed);
        System.out.println(getMessage("games_won") + gamesWon);
        System.out.println(getMessage("games_lost") + gamesLost);
        System.out.println(getMessage("credits") + playerCredits);
        System.out.println("==============================");
    }

    // Changes the language
    public static void changeLanguage(Scanner scanner) {
        System.out.println(getMessage("choose_lang"));
        System.out.println("1. English");
        System.out.println("2. Español");
        System.out.println("3. Português");
        System.out.println("4. Turkish");
        System.out.print(getMessage("menu_prompt"));
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            language = "EN";
            System.out.println(getMessage("lang_changed"));
        } else if (choice.equals("2")) {
            language = "ES";
            System.out.println(getMessage("lang_changed"));
        } else if (choice.equals("3")) {
            language = "PT";
            System.out.println(getMessage("lang_changed"));
        } else if (choice.equals("4")) {
            language = "TR";
            System.out.println(getMessage("lang_changed"));
        } else {
            System.out.println(getMessage("invalid_choice"));
        }
    }

    // Initializes a complete deck of cards
    public static List<String> initializeDeck() {
        String[] suits = {"♥", "♦", "♠", "♣"};
        String[] faces = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
        List<String> deck = new ArrayList<>();
        for (String suit : suits) {
            for (String face : faces) {
                deck.add(face + " of " + suit);
            }
        }
        return deck;
    }

    // Calculates the best hand value
    public static int calculateBestValue(List<String> hand) {
        int minValue = 0;
        int aceCount = 0;

        for (String card : hand) {
            String face = card.split(" ")[0];
            if (face.equals("Ace")) {
                minValue += 1;
                aceCount++;
            } else if (face.equals("Jack") || face.equals("Queen") || face.equals("King")) {
                minValue += 10;
            } else {
                minValue += Integer.parseInt(face);
            }
        }

        int maxValue = minValue;
        while (aceCount > 0 && maxValue + 10 <= 21) {
            maxValue += 10;
            aceCount--;
        }

        return maxValue;
    }

    // Formats the hand for display
    public static String formatHand(List<String> hand) {
        return hand.toString();
    }

    // Gets a message based on the selected language
    public static String getMessage(String key) {
        Map<String, String> messages = new HashMap<>();
        if (language.equals("EN")) {
            messages.put("welcome", "Welcome to Blackjack!");
            messages.put("start_game", "Start Game");
            messages.put("view_stats", "View Statistics");
            messages.put("change_lang", "Change Language");
            messages.put("quit", "Quit");
            messages.put("menu_prompt", "Choose an option: ");
            messages.put("place_bet", "Place your bet (Credits: " + playerCredits + "): ");
            messages.put("insufficient_credits", "You don't have enough credits to place this bet.");
            messages.put("invalid_bet", "Invalid bet. Please enter a valid amount.");
            messages.put("your_hand", "Your hand: ");
            messages.put("dealer_card", "Dealer's visible card: ");
            messages.put("your_total", "Your current total: ");
            messages.put("hit_stand", "Enter (1) Hit or (2) Stand: ");
            messages.put("you_drew", "You drew: ");
            messages.put("dealer_turn", "Dealer's turn.");
            messages.put("dealer_hits", "Dealer hits...");
            messages.put("dealer_drew", "Dealer drew: ");
            messages.put("dealer_total", "Dealer's total: ");
            messages.put("busted", "You busted with ");
            messages.put("blackjack", "Blackjack! You win!");
            messages.put("you_win", "Congratulations! You win!");
            messages.put("tie", "It's a tie!");
            messages.put("dealer_wins", "Dealer wins!");
            messages.put("exit_message", "Thanks for playing! Goodbye!");
            messages.put("no_credits", "You have no credits left to play.");
            messages.put("stats_title", "Game Statistics");
            messages.put("games_played", "Games Played: ");
            messages.put("games_won", "Games Won: ");
            messages.put("games_lost", "Games Lost: ");
            messages.put("credits", "Credits: ");
            messages.put("choose_lang", "Choose a language:");
            messages.put("lang_changed", "Language changed successfully.");
            messages.put("invalid_choice", "Invalid choice. Please try again.");
        } else if (language.equals("ES")) {
            messages.put("welcome", "¡Bienvenido a Blackjack!");
            messages.put("start_game", "Iniciar Juego");
            messages.put("view_stats", "Ver Estadísticas");
            messages.put("change_lang", "Cambiar Idioma");
            messages.put("quit", "Salir");
            messages.put("menu_prompt", "Elige una opción: ");
            messages.put("place_bet", "Coloca tu apuesta (Créditos: " + playerCredits + "): ");
            messages.put("insufficient_credits", "No tienes suficientes créditos para esta apuesta.");
            messages.put("invalid_bet", "Apuesta no válida. Ingresa un monto válido.");
            messages.put("your_hand", "Tu mano: ");
            messages.put("dealer_card", "Carta visible del dealer: ");
            messages.put("your_total", "Tu total actual: ");
            messages.put("hit_stand", "Ingresa (1) Pedir carta o (2) Plantarse: ");
            messages.put("you_drew", "Sacaste: ");
            messages.put("dealer_turn", "Turno del dealer.");
            messages.put("dealer_hits", "El dealer pide carta...");
            messages.put("dealer_drew", "El dealer sacó: ");
            messages.put("dealer_total", "Total del dealer: ");
            messages.put("busted", "Te pasaste con ");
            messages.put("blackjack", "¡Blackjack! ¡Ganaste!");
            messages.put("you_win", "¡Felicidades! ¡Ganaste!");
            messages.put("tie", "¡Es un empate!");
            messages.put("dealer_wins", "¡El dealer gana!");
            messages.put("exit_message", "¡Gracias por jugar! ¡Adiós!");
            messages.put("no_credits", "No tienes créditos suficientes para jugar.");
            messages.put("stats_title", "Estadísticas del Juego");
            messages.put("games_played", "Juegos Jugados: ");
            messages.put("games_won", "Juegos Ganados: ");
            messages.put("games_lost", "Juegos Perdidos: ");
            messages.put("credits", "Créditos: ");
            messages.put("choose_lang", "Elige un idioma:");
            messages.put("lang_changed", "Idioma cambiado exitosamente.");
            messages.put("invalid_choice", "Elección no válida. Intenta de nuevo.");
        } else if (language.equals("PT")) { 
            messages.put("welcome", "Bem-vindo ao Blackjack!");
            messages.put("start_game", "Iniciar Jogo");
            messages.put("view_stats", "Ver Estatísticas");
            messages.put("change_lang", "Mudar Idioma");
            messages.put("quit", "Sair");
            messages.put("menu_prompt", "Escolha uma opção: ");
            messages.put("place_bet", "Faça sua aposta (Créditos: " + playerCredits + "): ");
            messages.put("insufficient_credits", "Você não tem créditos suficientes para esta aposta.");
            messages.put("invalid_bet", "Aposta inválida. Insira um valor válido.");
            messages.put("your_hand", "Sua mão: ");
            messages.put("dealer_card", "Carta visível do dealer: ");
            messages.put("your_total", "Seu total atual: ");
            messages.put("hit_stand", "Digite (1) Pedir Carta ou (2) Parar: ");
            messages.put("you_drew", "Você tirou: ");
            messages.put("dealer_turn", "Turno do dealer.");
            messages.put("dealer_hits", "O dealer pede carta...");
            messages.put("dealer_drew", "O dealer tirou: ");
            messages.put("dealer_total", "Total do dealer: ");
            messages.put("busted", "Você estourou com ");
            messages.put("blackjack", "Blackjack! Você venceu!");
            messages.put("you_win", "Parabéns! Você venceu!");
            messages.put("tie", "Empate!");
            messages.put("dealer_wins", "O dealer venceu!");
            messages.put("exit_message", "Obrigado por jogar! Até a próxima!");
            messages.put("no_credits", "Você não tem créditos suficientes para jogar.");
            messages.put("stats_title", "Estatísticas do Jogo");
            messages.put("games_played", "Jogos Jogados: ");
            messages.put("games_won", "Jogos Vencidos: ");
            messages.put("games_lost", "Jogos Perdidos: ");
            messages.put("credits", "Créditos: ");
            messages.put("choose_lang", "Escolha um idioma:");
            messages.put("lang_changed", "Idioma alterado com sucesso.");
            messages.put("invalid_choice", "Escolha inválida. Tente novamente.");
        } else if (language.equals("TR")) { 
            messages.put("welcome", "Blackjack'a Hoş Geldiniz!");
            messages.put("start_game", "Oyunu Başlat");
            messages.put("view_stats", "İstatistikleri Görüntüle");
            messages.put("change_lang", "Dili Değiştir");
            messages.put("quit", "Çıkış");
            messages.put("menu_prompt", "Bir seçenek seçin: ");
            messages.put("place_bet", "Bahsinizi yapın (Krediler: " + playerCredits + "): ");
            messages.put("insufficient_credits", "Bu bahis için yeterli krediniz yok.");
            messages.put("invalid_bet", "Geçersiz bahis. Lütfen geçerli bir miktar girin.");
            messages.put("your_hand", "Eliniz: ");
            messages.put("dealer_card", "Krupiyenin görünen kartı: ");
            messages.put("your_total", "Mevcut toplamınız: ");
            messages.put("hit_stand", "(1) Kart Çek veya (2) Dur seçeneğini girin: ");
            messages.put("you_drew", "Çektiğiniz kart: ");
            messages.put("dealer_turn", "Krupiyenin sırası.");
            messages.put("dealer_hits", "Krupiye kart çekiyor...");
            messages.put("dealer_drew", "Krupiye şu kartı çekti: ");
            messages.put("dealer_total", "Krupiyenin toplamı: ");
            messages.put("busted", "Eliniz " + playerCredits + " ile patladı.");
            messages.put("blackjack", "Blackjack! Kazandınız!");
            messages.put("you_win", "Tebrikler! Kazandınız!");
            messages.put("tie", "Beraberlik!");
            messages.put("dealer_wins", "Krupiye kazandı!");
            messages.put("exit_message", "Oynadığınız için teşekkürler! Hoşça kalın!");
            messages.put("no_credits", "Oynamak için yeterli krediniz yok.");
            messages.put("stats_title", "Oyun İstatistikleri");
            messages.put("games_played", "Oynanan Oyunlar: ");
            messages.put("games_won", "Kazanılan Oyunlar: ");
            messages.put("games_lost", "Kaybedilen Oyunlar: ");
            messages.put("credits", "Krediler: ");
            messages.put("choose_lang", "Bir dil seçin:");
            messages.put("lang_changed", "Dil başarıyla değiştirildi.");
            messages.put("invalid_choice", "Geçersiz seçim. Lütfen tekrar deneyin.");
        
        }
        return messages.getOrDefault(key, "Message not found."); //check for any error 
    }

    // Visual feedback for win/loss/tie
    public static String visualWin() {
        return "\n🎉🎉🎉 YOU WIN! 🎉🎉🎉\n";
    }

    public static String visualLose() {
        return "\n💀💀💀 YOU LOSE! 💀💀💀\n";
    }

    public static String visualTie() {
        return "\n🤝🤝🤝 IT'S A TIE! 🤝🤝🤝\n";
    }

    public static String visualBust() {
        return "\n💥💥💥 BUST! 💥💥💥\n";
    }
}
