import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RockPaperScissorsGame extends JFrame {
    private JLabel resultLabel;
    private JLabel streakLabel;
    private JLabel computerChoiceLabel;
    private int winStreak = 0;
    private Timer animationTimer;
    private JLabel computerImageLabel;
    private int animationIndex = 0;
    private String[] choices = { "rock", "paper", "scissors" };
    private String currentComputerChoice;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RockPaperScissorsGame::new);
    }

    public RockPaperScissorsGame() {
        setTitle("じゃんけんゲーム");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
    // メインパネル
    JPanel mainPanel = new JPanel(new BorderLayout());
    
    // 上部：連勝表示
    streakLabel = new JLabel("連勝数: 0", SwingConstants.CENTER);
    streakLabel.setFont(new Font("Arial", Font.BOLD, 20));
    mainPanel.add(streakLabel, BorderLayout.NORTH);
    
    // 中央：相手の手と結果
    JPanel centerPanel = new JPanel(new GridLayout(3, 1));
    
    // 相手の手表示（画像付き）
    computerChoiceLabel = new JLabel("相手の手: ?", SwingConstants.CENTER);
    computerChoiceLabel.setFont(new Font("Arial", Font.BOLD, 16));
    centerPanel.add(computerChoiceLabel);
    
    // 相手の手画像
    computerImageLabel = new JLabel("", SwingConstants.CENTER);
    computerImageLabel.setPreferredSize(new Dimension(100, 100));
    centerPanel.add(computerImageLabel);
    
    // 結果表示
    resultLabel = new JLabel("結果: ", SwingConstants.CENTER);
    resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
    centerPanel.add(resultLabel);
    
    mainPanel.add(centerPanel, BorderLayout.CENTER);
    
    // 下部：じゃんけんボタン（画像サイズ調整）
    JPanel buttonPanel = new JPanel();
    
    // 画像サイズを調整
    ImageIcon rockIcon = new ImageIcon("images/rock.png");
    ImageIcon paperIcon = new ImageIcon("images/paper.png");
    ImageIcon scissorsIcon = new ImageIcon("images/scissors.png");
    
    // 画像をリサイズ
    Image rockImg = rockIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
    Image paperImg = paperIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
    Image scissorsImg = scissorsIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
    
    JButton rockButton = new JButton(new ImageIcon(rockImg));
    JButton paperButton = new JButton(new ImageIcon(paperImg));
    JButton scissorsButton = new JButton(new ImageIcon(scissorsImg));
    
    // ボタンアクション
    rockButton.addActionListener(e -> playGame("rock"));
    paperButton.addActionListener(e -> playGame("paper"));
    scissorsButton.addActionListener(e -> playGame("scissors"));
    
    buttonPanel.add(rockButton);
    buttonPanel.add(paperButton);
    buttonPanel.add(scissorsButton);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    // アニメーションタイマーを設定
    setupAnimationTimer();
    
    add(mainPanel);
}

    private void playGame(String playerChoice, JLabel computerImageLabel) {
        // 相手の手をランダムに選択
        String[] choices = { "rock", "paper", "scissors" };
        Random random = new Random();
        String computerChoice = choices[random.nextInt(3)];

        // 相手の手をテキストと画像で表示
        computerChoiceLabel.setText("相手の手: " + computerChoice);

        // 相手の手画像を表示
        ImageIcon computerIcon = new ImageIcon("images/" + computerChoice + ".png");
        Image computerImg = computerIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        computerImageLabel.setIcon(new ImageIcon(computerImg));

        // 勝敗判定
        String result = determineWinner(playerChoice, computerChoice);
        resultLabel.setText("結果: " + result);

        // 連勝数更新
        if (result.equals("あなたの勝ち！")) {
            winStreak++;
            streakLabel.setText("連勝数: " + winStreak);
        } else if (result.equals("あなたの負け")) {
            winStreak = 0;
            streakLabel.setText("連勝数: 0");
        }
    }

    private String determineWinner(String player, String computer) {
        if (player.equals(computer)) {
            return "あいこ";
        }

        if ((player.equals("rock") && computer.equals("scissors")) ||
                (player.equals("paper") && computer.equals("rock")) ||
                (player.equals("scissors") && computer.equals("paper"))) {
            return "あなたの勝ち！";
        }

        return "あなたの負け";
    }

    private void setupAnimationTimer() {
        animationTimer = new Timer(200, e -> { // 200ミリ秒ごとに変更
            // 次の手に切り替え
            animationIndex = (animationIndex + 1) % choices.length;
            String currentChoice = choices[animationIndex];

            // 画像を更新
            ImageIcon icon = new ImageIcon("images/" + currentChoice + ".png");
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            computerImageLabel.setIcon(new ImageIcon(img));
        });

        // アニメーション開始
        animationTimer.start();
    }

    private void playGame(String playerChoice) {
        // アニメーションを停止
        animationTimer.stop();

        // 現在表示されている手を相手の手として採用
        currentComputerChoice = choices[animationIndex];

        // 相手の手をテキストと画像で表示
        computerChoiceLabel.setText("相手の手: " + currentComputerChoice);

        // 勝敗判定
        String result = determineWinner(playerChoice, currentComputerChoice);
        resultLabel.setText("結果: " + result);

        // 連勝数更新
        if (result.equals("あなたの勝ち！")) {
            winStreak++;
            streakLabel.setText("連勝数: " + winStreak);
        } else if (result.equals("あなたの負け")) {
            winStreak = 0;
            streakLabel.setText("連勝数: 0");
        }

        // 2秒後にアニメーションを再開
        Timer restartTimer = new Timer(2000, e -> {
            resultLabel.setText("結果: ");
            computerChoiceLabel.setText("相手の手: ?");
            animationTimer.start();
        });
        restartTimer.setRepeats(false);
        restartTimer.start();
    }
}