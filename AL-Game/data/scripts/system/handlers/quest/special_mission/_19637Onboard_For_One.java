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
package quest.special_mission;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Created by Ace65 on 20/02/2016.
 */
public class _19637Onboard_For_One extends QuestHandler
{
    private final static int questId = 19637;
    private final static int[] mobs = {215500, 215501, 215502, 215503};

    public _19637Onboard_For_One() {
        super(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env);
    }

    @Override
    public void register() {
        qe.registerOnLevelUp(questId);
        qe.registerQuestNpc(798926).addOnQuestStart(questId);
        qe.registerQuestNpc(798926).addOnTalkEvent(questId);
        qe.registerQuestNpc(798926).addOnTalkEvent(questId);
        for (int mob: mobs) {
            qe.registerQuestNpc(mob).addOnKillEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        DialogAction dialog = env.getDialog();
        int targetId = env.getTargetId();
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        } if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 798926) {
                switch (dialog) {
                    case QUEST_SELECT:
                        return sendQuestDialog(env, 4762);
                    case QUEST_ACCEPT_1:
                    case QUEST_ACCEPT_SIMPLE:
                        return sendQuestStartDialog(env);
                    case QUEST_REFUSE_SIMPLE:
                        return closeDialogWindow(env);
				default:
					break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 798926: {
                    switch (dialog) {
                        case QUEST_SELECT: {
                            return sendQuestDialog(env, 10002);
                        } case SELECT_QUEST_REWARD: {
                            return sendQuestEndDialog(env);
                        } default:
                            return sendQuestEndDialog(env);
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798926) {
                switch (dialog) {
                    case SELECT_QUEST_REWARD: {
                        return sendQuestDialog(env, 5);
                    } default:
                        return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        } switch (targetId) {
            case 215500:
            case 215501:
            case 215502:
            case 215503:
                if (qs.getQuestVarById(1) < 10) {
                    qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
                    updateQuestStatus(env);
                } if (qs.getQuestVarById(1) >= 10) {
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
            }
                break;
        }
        return false;
    }
}
