/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.morheim;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Falke_34
 */
public class _23830ABundletoEnjoy extends QuestHandler {

    private final static int questId = 23830;

    public _23830ABundletoEnjoy() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
        qe.registerQuestNpc(798382).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        //Quest only available at Lvl30
        if (player.getLevel() == 30 && (qs == null || qs.getStatus() == QuestStatus.NONE)) {
            return QuestService.startQuest(env);
        }
        return false;
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        DialogAction dialog = env.getDialog();
        int targetId = env.getTargetId();

        if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (targetId == 798382) { // Herner
                if (dialog == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 10002);
                } else if (dialog == DialogAction.SELECT_QUEST_REWARD) {
                    changeQuestStep(env, 0, 0, true);
                    return sendQuestDialog(env, 5);
                }
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798382) { // Herner
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }
}