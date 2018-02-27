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
package quest.cygnea;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author pralinka
 * @rework FrozenKiller
 */
public class _10502CygneaSecrets extends QuestHandler {

    public static final int questId = 10502;

    public _10502CygneaSecrets() {
        super(questId);
    }

    @Override
    public void register() {
        int[] npcs = {804701, 804702, 731537, 702669};
		qe.registerOnLevelUp(questId);
        for (int npc : npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
		qe.registerQuestItem(182215601, questId);
		qe.registerOnEnterZone(ZoneName.get("LF5_ItemUseArea_Q10502"), questId);
    }

	@Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 10501, true);
    }
	@Override
    public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (!player.isInsideZone(ZoneName.get("LF5_ItemUseArea_Q10502"))) {
            return HandlerResult.UNKNOWN;
			}
            int var = qs.getQuestVarById(0);
            if (var == 4) {
				giveQuestItem(env, 182215602, 1);
                return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 5, false));
            }
        }
        return HandlerResult.FAILED;
    }
	
    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 804701) {
                switch (env.getDialog()) {
                    case QUEST_SELECT:
                        if (var == 0) {
                            return sendQuestDialog(env, 1011);
                        }						
                    case SETPRO1:
                        changeQuestStep(env, 0, 1, false); 
						return closeDialogWindow(env);
				default:
					break;
                }
            }
			if (targetId == 804702) {
                switch (env.getDialog()) {
                    case QUEST_SELECT:
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        } else if (var == 2) {
                            return sendQuestDialog(env, 1693);
                        } else if (var == 3) {
                            return sendQuestDialog(env, 2034);
                        } else if (var == 5) {
                            return sendQuestDialog(env, 2716);
                        }
                    case SETPRO2:
                        changeQuestStep(env, 1, 2, false); 
						return closeDialogWindow(env);
					case CHECK_USER_HAS_QUEST_ITEM:
						if (QuestService.collectItemCheck(env, true)) {	
							changeQuestStep(env, 2, 3, false);
							return sendQuestDialog(env, 10000);
						} else {
							return sendQuestDialog(env, 10001);
						}
					case SETPRO4:	
						giveQuestItem(env, 182215601, 1);
                        changeQuestStep(env, 3, 4, false);
                        return closeDialogWindow(env);
					case SETPRO6:	
						removeQuestItem(env, 182215602, 1);
                        changeQuestStep(env, 5, 6, false);
                        return closeDialogWindow(env);
				default:
					break;
                }
            }
			if (targetId == 702669) {
                switch (env.getDialog()) {
                    case USE_OBJECT:
                        return true;
				default:
					break; 
                }
            }
			if (targetId == 731537) {
                switch (env.getDialog()) {
                    case USE_OBJECT:
                        if (var == 6) {
                            return sendQuestDialog(env, 3057);
                        }
                    case SET_SUCCEED:
						giveQuestItem(env, 182215635, 1);
                        qs.setQuestVar(7);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return closeDialogWindow(env);
				default:
					break;
                }
            }		
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 804701) {
                if (env.getDialog() == DialogAction.QUEST_SELECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}
