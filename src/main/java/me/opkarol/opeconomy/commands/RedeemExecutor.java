package me.opkarol.opeconomy.commands;

import me.opkarol.opeconomy.redeem.CodeGenerator;
import me.opkarol.opeconomy.redeem.Database;
import me.opkarol.opeconomy.redeem.Redeem;
import me.opkarol.opeconomy.utils.ColorUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static me.opkarol.opeconomy.utils.ObjectUtils.*;
import static me.opkarol.opeconomy.utils.Utils.*;

public class RedeemExecutor extends Database implements CommandExecutor {
    private static String dontHavePermission;
    private static String notAvailableToConsole;
    private static String validCodeEntered;
    private static String notValidCodeEntered;
    private static String lastArgumentNotNumber;
    private static String createdCode;
    private static String badUsage;
    private static String removedCode;
    private static String infoMessage;
    private static int codeLength;
    private static boolean removeAdditional;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("opeconomy.command.redeem.use") && !sender.isOp())
            return returnMessageToSender(sender, dontHavePermission);

        if (!(sender instanceof Player)) return returnMessageToSender(sender, notAvailableToConsole);

        if (!sender.hasPermission("opeconomy.command.redeem.admin") && !sender.isOp()) return returnMessageToSender(sender, dontHavePermission);

        if (args.length == 0) return returnMessageToSender(sender, badUsage);

        String argumentFirst = args[0];
        if (hasPlayerUsedCode(argumentFirst, sender)) return returnMessageToSender(sender, notValidCodeEntered);

        if (args.length == 1) { if (argumentFirst.equalsIgnoreCase("remove") || argumentFirst.equalsIgnoreCase("info") || argumentFirst.equalsIgnoreCase("create")) { return returnMessageToSender(sender, badUsage); } if (Database.playerUsedValidCode(argumentFirst, (Player) sender)) return returnMessageToSender(sender, validCodeEntered.replace("%redeem_reward%", String.valueOf(getRedeemFromCode(argumentFirst).getReward()))); else return returnMessageToSender(sender, notValidCodeEntered);}

        if (args.length == 4 && argumentFirst.equalsIgnoreCase("create")) {
            // /command create [CODE (Random - Provided)] [Max Uses] [Reward]
            String code = args[1];
            String maxUsesString = args[2];
            String rewardString = args[3];

            if ("random".equalsIgnoreCase(code)) code = CodeGenerator.getString(codeLength, CodeGenerator.Mode.ALPHANUMERIC);

            int codeLengthValue = code.length();
            if (removeAdditional) if (codeLengthValue != codeLength) if (codeLengthValue < codeLength) code = code + CodeGenerator.getString(codeLength - codeLengthValue, CodeGenerator.Mode.ALPHANUMERIC); else code = code.substring(0, codeLength);

            String maxUsesString2 = replaceDigits(maxUsesString);

            if (isntValidInteger(maxUsesString2)) return returnMessageToSender(sender, lastArgumentNotNumber);

            int maxUsesAmount;
            try { maxUsesAmount = Integer.parseInt(maxUsesString2);} catch (NumberFormatException e) {return returnMessageToSender(sender, lastArgumentNotNumber);}

            String rewardString2 = replaceDigits(rewardString);

            if (isntValidInteger(rewardString2)) return returnMessageToSender(sender, lastArgumentNotNumber);

            int rewardAmount;
            try { rewardAmount = Integer.parseInt(rewardString2);} catch (NumberFormatException e) {return returnMessageToSender(sender, lastArgumentNotNumber);}

            addCodeToMap(code, new Redeem(maxUsesAmount, 0, rewardAmount));

            TextComponent textComponent = createComponent(" &f[Click to copy code]"); setFullTextComponentCopy(textComponent, code, HoverType.Copy); TextComponent main = createComponent(createdCode.replace("%redeem_code_name%", code)); main.addExtra(textComponent);

            return returnMessageToSender(sender, main);
        }

        String code = args[1];
        switch (argumentFirst) {
            case "remove" -> {
                if (isntCodeExists(code)) return returnMessageToSender(sender, notValidCodeEntered);

                removeCodeFromMap(code);
                return returnMessageToSender(sender, removedCode);
            }
            case "info" -> {
                if (isntCodeExists(code)) return returnMessageToSender(sender, notValidCodeEntered);

                Redeem redeem = getRedeemFromCode(code);
                return returnMessageToSender(sender, infoMessage.replace("%redeem_code_name%", code).replace("%redeem_reward%", String.valueOf(redeem.getReward())).replace("%redeem_maxUses%", String.valueOf(redeem.getMaxUses())).replace("%redeem_uses%", String.valueOf(redeem.getUses())).replace("%redeem_players_name%", Arrays.toString(redeem.getUsed().toArray())));

            }
        }

        return returnMessageToSender(sender, badUsage);
    }

    public static TextComponent createComponent(String name){
        return new TextComponent(ColorUtils.formatText(name));
    }

    public static void setDontHavePermission(String dontHavePermission) {
        RedeemExecutor.dontHavePermission = dontHavePermission;
    }

    public static void setNotAvailableToConsole(String notAvailableToConsole) {
        RedeemExecutor.notAvailableToConsole = notAvailableToConsole;
    }

    public static void setLastArgumentNotNumber(String lastArgumentNotNumber) {
        RedeemExecutor.lastArgumentNotNumber = lastArgumentNotNumber;
    }

    public static void setBadUsage(String badUsage) {
        RedeemExecutor.badUsage = badUsage;
    }

    public static void setCreatedCode(String createdCode) {
        RedeemExecutor.createdCode = createdCode;
    }

    public static void setInfoMessage(String infoMessage) {
        RedeemExecutor.infoMessage = infoMessage;
    }

    public static void setNotValidCodeEntered(String notValidCodeEntered) {
        RedeemExecutor.notValidCodeEntered = notValidCodeEntered;
    }

    public static void setRemovedCode(String removedCode) {
        RedeemExecutor.removedCode = removedCode;
    }

    public static void setValidCodeEntered(String validCodeEntered) {
        RedeemExecutor.validCodeEntered = validCodeEntered;
    }

    public static void setCodeLength(int codeLength) {
        RedeemExecutor.codeLength = codeLength;
    }

    public static void setRemoveAdditional(boolean removeAdditional) {
        RedeemExecutor.removeAdditional = removeAdditional;
    }
}
