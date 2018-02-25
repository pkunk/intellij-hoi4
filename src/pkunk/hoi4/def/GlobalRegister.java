package pkunk.hoi4.def;

import java.util.*;

import static pkunk.hoi4.def.BlockNode.*;
import static pkunk.hoi4.def.Scope.*;
import static pkunk.hoi4.def.Type.*;

public class GlobalRegister {

    public static final List<Node> NODES = new ArrayList<>();
    public static final Map<Scope, Map<String, Node>> CONDITIONS_MAP = new HashMap<>();
    public static final Map<String, Node> ACTIONS = new HashMap<>();

    static {

        NODES.addAll(Arrays.asList(

                // Keywords
                newConditionScope("limit", INHERITED, INHERITED)

                , newConditionScope("NOT", INHERITED, INHERITED)
                        .withConditions(true)
                , newConditionScope("OR", INHERITED, INHERITED)
                        .withConditions(true)
                , newConditionScope("AND", INHERITED, INHERITED)
                        .withConditions(true)

                , newBlock("news_event", EVENTS, COUNTRY)
                        .withMandatoryChild("id", ID)
                        .withChild("title", ID)
                        .withChild("desc", ID)
                        .withChild("picture", ID)
                        .withChild("major", BOOLEAN)
                        .withChild("is_triggered_only", BOOLEAN)
                        .withChild("hidden", BOOLEAN)
                        .withChild("fire_only_once", BOOLEAN)
                        .withChild("trigger", BLOCK)
                        .withChild("immediate", BLOCK)
                        .withChild("mean_time_to_happen", BLOCK)
                        .withChild("fire_for_sender", BOOLEAN)
                        .withChild("show_major", BLOCK)
                        .withMultiChild("option", BLOCK)

                , newBlock("country_event", EVENTS, COUNTRY)
                        .withMandatoryChild("id", ID)
                        .withChild("title", ID)
                        .withChild("desc", ID)
                        .withChild("picture", ID)
                        .withChild("is_triggered_only", BOOLEAN)
                        .withChild("hidden", BOOLEAN)
                        .withChild("fire_only_once", BOOLEAN)
                        .withChild("trigger", BLOCK)
                        .withChild("immediate", BLOCK)
                        .withChild("mean_time_to_happen", BLOCK)
                        .withMultiChild("option", BLOCK)

                , newConditionScope("trigger", INHERITED, INHERITED)
                , newBlock("mean_time_to_happen", INHERITED, INHERITED)
                        .withChild("modifier", BLOCK)
                        .withChild("hours", INTEGER)
                        .withChild("days", INTEGER)
                        .withChild("months", INTEGER)
                        .withChild("years", INTEGER)

                , newBlock("option", INHERITED, INHERITED)
                        .withChild("name", ID)
                        .withChild("trigger", BLOCK)
                        .withChild("ai_chance", BLOCK)
                        .withActions(true)

                , newBlock("ai_chance", INHERITED, AI)
                    .withChild("factor", FLOAT)
                    .withChild("base", INTEGER)
                    .withMultiChild("modifier", BLOCK)

                , newConditionScope("modifier", AI, COUNTRY)
                        .withChild("ai_irrationality", FLOAT)
                        .withChild("factor", FLOAT)
                        .withChild("add", INTEGER)

                , newBlock("focus_tree", FOCUS_TREES, FOCUSES)
                    .withMandatoryChild("id", ID)
                    .withMandatoryChild("country", BLOCK)
                    .withChild("default", BOOLEAN)
                    .withChild("reset_on_civilwar", BOOLEAN)
                    .withChild("continuous_focus_position", BLOCK)
                    .withMultiChild("focus", BLOCK)

                , newConditionScope("country", FOCUSES, AI)
                        .withChild("factor", FLOAT)

                , newBlock("focus", FOCUSES, FOCUS)
                        .withMandatoryChild("id", ID)
                        .withMandatoryChild("icon", ID)
                        .withChild("text", ID)
                        .withChild("prerequisite", BLOCK)
                        .withChild("mutually_exclusive", BLOCK)
                        .withMultiChild("will_lead_to_war_with", TAG)
                        .withMandatoryChild("x", INTEGER)
                        .withMandatoryChild("y", INTEGER)
                        .withChild("relative_position_id", ID)
                        .withChild("cost", INTEGER)
                        .withChild("ai_will_do", BLOCK)
                        .withChild("historical_ai", BLOCK)
                        .withChild("available", BLOCK)
                        .withChild("bypass", BLOCK)
                        .withChild("cancel", BLOCK)
                        .withChild("cancel_if_invalid", BOOLEAN)
                        .withChild("continue_if_invalid", BOOLEAN)
                        .withChild("available_if_capitulated", BOOLEAN)
                        .withChild("select_effect", BLOCK)
                        .withChild("complete_tooltip", BLOCK)
                        .withMandatoryChild("completion_reward", BLOCK)

                , newConditionScope("ai_will_do", FOCUS, AI)
                    .withMandatoryChild("factor", INTEGER)
                    .withChild("modifier", BLOCK)
                , newConditionScope("historical_ai", FOCUS, COUNTRY)
                , newConditionScope("available", FOCUS, COUNTRY)
                , newConditionScope("bypass", FOCUS, COUNTRY)
                , newConditionScope("cancel", FOCUS, COUNTRY)
                , newActionScope("select_effect", FOCUS, COUNTRY)
                , newActionScope("complete_tooltip", FOCUS, COUNTRY)
                , newActionScope("completion_reward", FOCUS, COUNTRY)

                ,newConditionScope("custom_trigger_tooltip", COUNTRY, COUNTRY)
                    .withMandatoryChild("tooltip", ID)

                // Scopes
                , newConditionScope("any_other_country", COUNTRY, COUNTRY) //not in wiki
                , newActionScope("every_other_country", COUNTRY, COUNTRY) //not in wiki
                , newActionScope("random_other_country", COUNTRY, COUNTRY) //not in wiki
                , newConditionScope("controller", STATE, COUNTRY) //not in wiki
                , newConditionScope("owner", STATE, COUNTRY) //not in wiki
                , newConditionScope("states_filter", COUNTRY, STATE) //not in wiki
                , newConditionScope("all_owned_state", COUNTRY, STATE) //not in wiki
                , newConditionScope("any_home_area_neighbor_country", COUNTRY, COUNTRY) //not in wiki
                , newConditionActionScope("random_owned_controlled_state", COUNTRY, STATE)
                    .withChild("prioritize", ARRAY)
                , newConditionActionScope("OVERLORD", COUNTRY, COUNTRY)
//                 ,newConditionActionScope("state_id", ALL, STATE)
                , newConditionScope("any_neighbor_country", COUNTRY, COUNTRY)
//                 ,newConditionActionScope("TAG", COUNTRY, COUNTRY)
                , newConditionScope("any_country", ALL, COUNTRY)
                , newConditionScope("any_enemy_country", COUNTRY, COUNTRY)
                , newConditionActionScope("all_neighbor_country", COUNTRY, COUNTRY)
                , newConditionActionScope("all_country", ALL, COUNTRY)
                , newConditionActionScope("all_enemy_country", COUNTRY, COUNTRY)
                , newConditionScope("any_state", ALL, STATE)
                , newConditionScope("any_owned_state", COUNTRY, STATE)
                , newConditionScope("any_neighbor_state", STATE, STATE)
                , newConditionScope("any_allied_country", COUNTRY, COUNTRY)
                , newConditionActionScope("all_state", ALL, STATE)
                , newConditionActionScope("every_owned_state", COUNTRY, STATE)
                , newConditionActionScope("all_neighbor_state", STATE, STATE)
                , newConditionActionScope("all_allied_country", COUNTRY, COUNTRY)
                , newActionScope("every_country", ALL, COUNTRY)
                , newActionScope("random_country", ALL, COUNTRY)
                , newConditionActionScope("every_neighbor_country", COUNTRY, COUNTRY)
                , newActionScope("random_neighbor_country", COUNTRY, COUNTRY)
                , newActionScope("every_enemy_country", COUNTRY, COUNTRY)
                , newActionScope("random_enemy_country", COUNTRY, COUNTRY)
                , newActionScope("random_state", ALL, STATE)
                , newActionScope("random_owned_state", COUNTRY, STATE)
                        .withChild("prioritize", ARRAY)
                , newActionScope("random_neighbor_state", STATE, STATE)
                , newActionScope("every_state", STATE, STATE)
                , newActionScope("every_owned_state", COUNTRY, STATE)
                , newActionScope("every_neighbor_state", STATE, STATE)
                , newConditionActionScope("capital_scope", COUNTRY, STATE)

                // Conditions
                , newCondition("has_dlc", ALL, STRING) //not in wiki
                , newCondition("has_cosmetic_tag", COUNTRY, ID) //not in wiki
                , newCondition("is_owned_and_controlled_by", STATE, TAG) //not in wiki
                , newCondition("state", STATE, INTEGER) //State //not in wiki
                , newCondition("has_wargoal_against", COUNTRY, TAG) //not in wiki
                , newCondition("is_justifying_wargoal_against", COUNTRY, TAG) //not in wiki
                , newBlockCondition("amount_taken_ideas", COUNTRY) //not in wiki
                    .withMandatoryChild("amount", INTEGER)
                    .withMandatoryChild("slots", ARRAY)
                , newCondition("is_researching_technology", COUNTRY, ID) //not in wiki
                , newCondition("is_island_state", STATE, BOOLEAN) //not in wiki
                , newCondition("is_in_tech_sharing_group", COUNTRY, ID) //not in wiki
                , newCondition("original_research_slots", COUNTRY, INTEGER) //not in wiki
                , newBlockCondition("has_volunteers_amount_from", COUNTRY) //not in wiki
                        .withMandatoryChild("tag", TAG)
                        .withChild("count", INTEGER)
                , newCondition("compare_autonomy_state", COUNTRY, ID) //not in wiki
                , newCondition("alliance_strength_ratio", COUNTRY, FLOAT) //not in wiki
                , newCondition("has_non_aggression_pact_with", COUNTRY, TAG) //not in wiki
                , newCondition("amount_research_slots", COUNTRY, INTEGER)
                , newCondition("num_divisions", COUNTRY, INTEGER)
                , newCondition("has_focus_tree", COUNTRY, ID)
                , newCondition("is_staging_coup", COUNTRY, BOOLEAN)
                , newCondition("has_full_control_of_state", COUNTRY, INTEGER) //State
                , newCondition("has_army_experience", COUNTRY, INTEGER)
                , newCondition("has_air_experience", COUNTRY, INTEGER)
                , newCondition("has_navy_experience", COUNTRY, INTEGER)
                , newCondition("has_country_leader", COUNTRY, BLOCK) //todo
                , newCondition("has_tech_bonus", COUNTRY, BLOCK) //todo
                , newCondition("has_resources_amount", COUNTRY, BLOCK) //todo
                , newCondition("num_of_subjects", COUNTRY, INTEGER)
                , newCondition("num_tech_sharing_groups", COUNTRY, INTEGER)
                , newCondition("compare_autonomy_progress_ratio", COUNTRY, FLOAT)
//                , newCondition("ai_irrationality", AI, INTEGER)
//                , newCondition("ai_liberate_desire", AI, INTEGER)
                , newCondition("always", ALL, BOOLEAN)
                , newCondition("any_claim", COUNTRY, BOOLEAN)
                , newCondition("any_war_score", COUNTRY, INTEGER)
//                , newCondition("armor", COMBAT, )
                , newCondition("can_research", COUNTRY, ID)
                , newCondition("controls_state", COUNTRY, INTEGER) //State
                , newCondition("country_exists", ALL, TAG)
                , newCondition("date", ALL, DATE)
//                , newCondition("dig_in", COMBAT, )
//                , newCondition("distance_to", STATE, )
                , newCondition("divisions_in_state", INHERITED, BLOCK) //todo
                , newCondition("exists", COUNTRY, BOOLEAN)
//                , newCondition("fastest_unit", COMBAT, )
                , newCondition("focus_progress", COUNTRY, BLOCK) //todo
                , newCondition("free_building_slots", STATE, BLOCK) //todo
//                , newCondition("frontage_full", COMBAT, )
                , newCondition("gives_military_access_to", COUNTRY, TAG)
//                , newCondition("hardness", COMBAT, )
                , newCondition("has_added_tension_amount", COUNTRY, FLOAT)
                , newCondition("has_annex_war_goal", COUNTRY, TAG)
                , newCondition("has_army_manpower", COUNTRY, BLOCK) //todo
                , newCondition("has_army_size", COUNTRY, BLOCK) //todo
                , newCondition("has_autonomy_state", COUNTRY, ID)
                , newBlockCondition("has_available_idea_with_traits", COUNTRY)
                        .withMandatoryChild("idea", ID)
                        .withChild("limit", INTEGER)
                , newCondition("has_built", COUNTRY, BLOCK) //todo
                , newCondition("has_capitulated", COUNTRY, BOOLEAN)
                , newCondition("has_carrier_airwings_on_mission", COUNTRY, BOOLEAN) //todo
                , newCondition("has_civil_war", COUNTRY, BOOLEAN)
//                , newCondition("has_combat_modifier", COMBAT, )
                , newCondition("has_completed_focus", COUNTRY, ID)
                , newCondition("has_country_flag", COUNTRY, ID) //Flag
                , newCondition("has_defensive_war", COUNTRY, BOOLEAN)
                , newCondition("has_defensive_war_with", COUNTRY, TAG)
                , newCondition("has_deployed_air_force_size", COUNTRY, INTEGER)
                , newCondition("has_equipment", COUNTRY, BLOCK) //todo
//                , newCondition("has_flanked_opponent", COMBAT, )
                , newCondition("has_global_flag", ALL, ID) //Flag
                , newCondition("has_government", COUNTRY, ID) //Ideology
                , newCondition("has_guaranteed", COUNTRY, TAG)
                , newCondition("has_idea", COUNTRY, ID)
                , newCondition("has_idea_with_trait", COUNTRY, ID)
                , newCondition("has_manpower", COUNTRY, INTEGER)
                , newCondition("has_manpower_for_recruit_change_to", COUNTRY, BLOCK) //todo
//                , newCondition("has_max_planning", COMBAT, BOOLEAN)
                , newCondition("has_military_access_to", COUNTRY, TAG)
                , newCondition("has_national_unity", COUNTRY, INTEGER) //todo deprecated
                , newCondition("has_navy_size", COUNTRY, BLOCK) //todo
                , newCondition("has_offensive_war", COUNTRY, BOOLEAN)
                , newCondition("has_offensive_war_with", COUNTRY, TAG)
                , newCondition("has_opinion", COUNTRY, BLOCK) //todo
                , newCondition("has_opinion_modifier", COUNTRY, ID)
                , newCondition("has_political_power", COUNTRY, INTEGER)
//                , newCondition("has_reserves", COMBAT, )
                , newCondition("has_start_date", ALL, DATE)
                , newCondition("has_state_flag", STATE, ID) //Flag
                , newCondition("has_tech", COUNTRY, ID)
                , newCondition("has_template_containing_unit", COUNTRY, ID)
//                , newCondition("has_trait", LEADER, ID)
                , newCondition("has_volunteers_from", COUNTRY, TAG)
                , newCondition("has_war", COUNTRY, BOOLEAN)
                , newCondition("has_war_together_with", COUNTRY, TAG)
                , newCondition("has_war_with", COUNTRY, TAG)
                , newCondition("has_war_with_amount", COUNTRY, INTEGER)
                , newCondition("ic_ratio", COUNTRY, BLOCK) //todo
                , newCondition("is_ai", COUNTRY, BOOLEAN)
//                , newCondition("is_amphibious_invasion", COMBAT, BOOLEAN)
//                , newCondition("is_attacker", COMBAT, BOOLEAN)
                , newCondition("is_border_conflict", STATE, BOOLEAN)
                , newCondition("is_claimed_by", STATE, TAG)
                , newCondition("is_coastal", STATE, BOOLEAN)
                , newCondition("is_controlled_by", STATE, TAG)
                , newCondition("is_core_of", STATE, TAG)
//                , newCondition("is_defender", COMBAT, BOOLEAN)
                , newCondition("is_demilitarized_zone", STATE, BOOLEAN)
                , newCondition("is_faction_leader", COUNTRY, BOOLEAN)
//                , newCondition("is_fighting_air_units", , BOOLEAN)
//                , newCondition("is_fighting_in_terrain", COMBAT, ID)
                , newCondition("is_guaranteed_by", COUNTRY, TAG)
                , newCondition("is_historical_focus_on", ALL, BOOLEAN)
                , newCondition("is_in_faction", COUNTRY, BOOLEAN)
                , newCondition("is_in_faction_with", COUNTRY, TAG)
                , newCondition("is_in_home_area", STATE, BOOLEAN)
                , newCondition("is_ironman", ALL, BOOLEAN)
                , newCondition("is_lend_leasing", COUNTRY, TAG)
                , newCondition("is_major", COUNTRY, BOOLEAN)
                , newCondition("is_neighbor_of", COUNTRY, TAG)
                , newCondition("is_on_continent", STATE, ID)
                , newCondition("is_owned_by", STATE, TAG)
                , newCondition("is_puppet", COUNTRY, BOOLEAN)
                , newCondition("is_puppet_of", COUNTRY, TAG)
                , newCondition("is_subject", COUNTRY, BOOLEAN)
                , newCondition("is_subject_of", COUNTRY, TAG)
//                , newCondition("is_winning", COMBAT, BOOLEAN)
//                , newCondition("land_doctrine_level", COUNTRY, ) //todo
//                , newCondition("less_combat_width_than_opponent", COMBAT, BOOLEAN)
//                , newCondition("night", COMBAT, BOOLEAN)
                , newCondition("num_of_available_civilian_factories", COUNTRY, INTEGER)
                , newCondition("num_of_available_military_factories", COUNTRY, INTEGER)
                , newCondition("num_of_available_naval_factories", COUNTRY, INTEGER)
                , newCondition("num_of_civilian_factories", COUNTRY, INTEGER)
                , newCondition("num_of_controlled_states", COUNTRY, INTEGER)
                , newCondition("num_of_factories", COUNTRY, INTEGER)
                , newCondition("num_of_military_factories", COUNTRY, INTEGER)
                , newCondition("num_of_naval_factories", COUNTRY, INTEGER)
                , newCondition("num_of_nukes", COUNTRY, INTEGER)
                , newCondition("original_tag", COUNTRY, TAG)
                , newCondition("owns_state", COUNTRY, INTEGER) //State
//                , newCondition("phase", COMBAT, ID)
//                , newCondition("recon_advantage", COMBAT, BOOLEAN)
                , newCondition("region", STATE, INTEGER)
//                , newCondition("reserves", , )
//                , newCondition("ships_in_area", AREA, BLOCK)
                , newBlockCondition("ships_in_state_ports", COUNTRY)
                    .withMandatoryChild("state", INTEGER) //State
                    .withMandatoryChild("size", INTEGER)
                    .withChild("type", ID)
//                , newCondition("skill", LEADER, INTEGER)
//                , newCondition("skill_advantage", COMBAT, )
                , newCondition("state_population", STATE, INTEGER)
                , newCondition("strength_ratio", COUNTRY, BLOCK) //todo
                , newCondition("surrender_progress", COUNTRY, FLOAT)
                , newCondition("tag", COUNTRY, TAG)
//                , newCondition("temperature", PROVINCE, )
                , newCondition("threat", ALL, FLOAT)
//                , newCondition("ideology", COUNTRY, FLOAT) //todo

                // Diplomatic and Political Commands
                , newAction("add_relation_modifier", COUNTRY, BLOCK) //todo
                , newAction("remove_relation_modifier", COUNTRY, ID)
                , newAction("add_political_power", COUNTRY, INTEGER)
                , newAction("add_opinion_modifier", COUNTRY, BLOCK) //todo
                , newAction("remove_opinion_modifier", COUNTRY, BLOCK) //todo
                , newAction("set_political_party", COUNTRY, BLOCK) //todo
                , newAction("set_politics", COUNTRY, BLOCK) //todo
                , newAction("set_political_power", COUNTRY, INTEGER)
                , newAction("create_faction", COUNTRY, STRING)
                , newAction("annex_country", COUNTRY, BLOCK) //todo
                , newAction("dismantle_faction", COUNTRY, BOOLEAN)
                , newAction("add_to_faction", COUNTRY, TAG)
                , newAction("remove_from_faction", COUNTRY, TAG)
                , newAction("give_guarantee", COUNTRY, TAG)
                , newAction("give_military_access", COUNTRY, TAG)
                , newAction("declare_war_on", COUNTRY, BLOCK) //todo
                , newAction("add_national_unity", COUNTRY, FLOAT)
//                , newAction("add_scaled_political_power", COUNTRY, BLOCK) //todo
                , newAction("hold_election", COUNTRY, TAG)
                , newAction("add_popularity", COUNTRY, BLOCK) //todo
                , newAction("start_civil_war", COUNTRY, BLOCK) //todo
                , newAction("create_wargoal", COUNTRY, BLOCK) //todo
                , newAction("set_national_unity", COUNTRY, FLOAT)
                , newAction("add_threat", COUNTRY, INTEGER)
                , newAction("add_named_threat", COUNTRY, BLOCK) //todo
                , newAction("set_rule", COUNTRY, BLOCK) //todo
                , newAction("diplomatic_relation", COUNTRY, BLOCK) //todo
                , newAction("create_import", COUNTRY, BLOCK) //todo
                , newAction("set_major", COUNTRY, BOOLEAN)
                , newAction("set_party_name", COUNTRY, BLOCK) //todo
                , newAction("reverse_add_opinion_modifier", COUNTRY, BLOCK) //todo
                , newAction("add_to_war", COUNTRY, BLOCK) //todo
                , newAction("change_tag_from", COUNTRY, TAG)
                , newAction("white_peace", COUNTRY, TAG)

                // Puppet and Autonomy Commands
                , newAction("add_autonomy_ratio", COUNTRY, BLOCK) //todo
                , newAction("set_autonomy", COUNTRY, BLOCK) //todo
                , newAction("release", COUNTRY, TAG)
                , newAction("puppet", COUNTRY, TAG)
                , newAction("release_puppet", COUNTRY, TAG)
                , newAction("end_puppet", COUNTRY, TAG)

                // Leader Commands
                , newAction("create_country_leader", COUNTRY, BLOCK) //todo
                , newAction("set_country_leader_ideology", COUNTRY, BLOCK) //todo
                , newAction("add_country_leader_trait", COUNTRY, ID)
                , newAction("remove_country_leader_trait", COUNTRY, ID)
                , newAction("create_navy_leader", COUNTRY, BLOCK) //todo
                , newAction("create_field_marshal", COUNTRY, BLOCK) //todo
                , newAction("create_corps_commander", COUNTRY, BLOCK) //todo
                , newAction("remove_unit_leader_trait", COUNTRY, BLOCK) //todo
                , newAction("add_unit_leader_trait", COUNTRY, BLOCK) //todo
                , newAction("keep_unit_leaders", COUNTRY, ARRAY)
                , newAction("remove_unit_leader", COUNTRY, ID)
                , newAction("retire_country_leader", COUNTRY, BOOLEAN)
                , newAction("kill_country_leader", COUNTRY, BOOLEAN)

                // Army Commands
                , newAction("add_manpower", COUNTRY, INTEGER)
                , newAction("create_unit", COUNTRY, BLOCK) //todo
                , newAction("division_template", COUNTRY, BLOCK) //todo
                , newAction("load_oob", COUNTRY, ID)
                , newAction("army_experience", COUNTRY, INTEGER)
                , newAction("navy_experience", COUNTRY, INTEGER)
                , newAction("air_experience", COUNTRY, INTEGER)

                // Equipment Commands
                , newAction("create_equipment_variant", COUNTRY, BLOCK) //todo
                , newAction("add_equipment_production", COUNTRY, BLOCK) //todo
                , newAction("add_ace", COUNTRY, BLOCK) //todo
                , newAction("add_equipment_to_stockpile", COUNTRY, BLOCK) //todo
                , newAction("send_equipment", COUNTRY, BLOCK) //todo
                , newAction("set_equipment_fraction", COUNTRY, FLOAT)
                , newAction("create_production_license", COUNTRY, BLOCK) //todo
                , newAction("transfer_ship", COUNTRY, BLOCK) //todo

                // National Focus Commands
                , newAction("unlock_national_focus", COUNTRY, ID)
                , newAction("complete_national_focus", COUNTRY, ID)

                // Ideas Commands
                , newAction("add_ideas", COUNTRY, ID)
                , newAction("add_timed_idea", COUNTRY, BLOCK) //todo
                , newAction("swap_ideas", COUNTRY, BLOCK) //todo
                , newAction("remove_ideas", COUNTRY, ID)
                , newAction("remove_ideas_with_trait", COUNTRY, ID)

                // Event Commands
                , newAction("country_event", COUNTRY, ID) //todo
                , newAction("country_event", COUNTRY, BLOCK) //todo
                , newAction("state_event", COUNTRY, ID)
                , newAction("news_event", COUNTRY, ID)
                , newAction("save_event_target_as", COUNTRY, ID)
                , newAction("save_global_event_target_as", COUNTRY, ID)
                , newAction("clear_global_event_target", COUNTRY, ID)
                , newAction("clear_global_event_targets", COUNTRY, ID)

                // Flag Commands
                , newAction("set_state_flag", STATE, ID)
                , newAction("clr_state_flag", STATE, ID)
                , newAction("set_country_flag", COUNTRY, ID) //todo
                , newAction("set_country_flag", COUNTRY, BLOCK) //todo
                , newAction("modify_country_flag", COUNTRY, BLOCK) //todo
                , newAction("clr_country_flag", COUNTRY, ID)
                , newAction("set_global_flag", COUNTRY, ID)
                , newAction("clr_global_flag", COUNTRY, ID)

                // State and Province Commands
                , newAction("set_state_name", STATE, STRING)
                , newAction("reset_state_name", STATE, BOOLEAN)
                , newAction("set_province_name", ALL, BLOCK) //todo
                , newAction("reset_province_name", ALL, INTEGER) //Province
                , newAction("add_state_core", COUNTRY, INTEGER) //State
                , newAction("remove_state_core", COUNTRY, INTEGER) //State
                , newAction("set_capital", COUNTRY, INTEGER) //State
                , newAction("add_state_claim", COUNTRY, INTEGER) //State
                , newAction("remove_state_claim", COUNTRY, INTEGER) //State
                , newAction("set_state_owner", COUNTRY, INTEGER) //State
                , newAction("set_state_controller", COUNTRY, INTEGER) //State
                , newAction("transfer_state", COUNTRY, INTEGER) //State
                , newAction("add_claim_by", STATE, TAG)
                , newAction("remove_claim_by", STATE, TAG)
                , newAction("add_core_of", STATE, TAG)
                , newAction("remove_core_of", STATE, TAG)
                , newAction("set_demilitarized_zone", STATE, BOOLEAN)
                , newAction("set_border_war", STATE, BOOLEAN)
                , newAction("set_province_controller", COUNTRY, INTEGER) //Province
                , newAction("goto_province", ALL, INTEGER) //Province
                , newAction("goto_state", ALL, INTEGER) //State
                , newAction("add_extra_state_shared_building_slots", STATE, INTEGER)

                // Buildings and Industrial Commands
                , newAction("set_building_level", STATE, BLOCK) //todo
                , newAction("damage_building", STATE, BLOCK) //todo
                , newAction("remove_building", STATE, BLOCK) //todo
                , newAction("add_building_construction", STATE, BLOCK) //todo
                , newAction("add_resource", ALL, BLOCK) //todo

                // Research Commands
                , newAction("add_research_slot", COUNTRY, INTEGER)
                , newAction("set_research_slots", COUNTRY, INTEGER)
                , newAction("add_tech_bonus", COUNTRY, BLOCK) //todo
                , newAction("add_to_tech_sharing_group", COUNTRY, ID)
                , newAction("remove_from_tech_sharing_group", COUNTRY, ID)
                , newAction("modify_tech_sharing_bonus", COUNTRY, BLOCK) //not in wiki //todo
                , newAction("set_technology", COUNTRY, BLOCK) //todo

                // Miscellaneous Commands
                , newAction("set_cosmetic_tag", COUNTRY, ID)
                , newAction("drop_cosmetic_tag", COUNTRY, BOOLEAN)
                , newAction("random", INHERITED, BLOCK) //todo Hate It
                , newAction("random_list", COUNTRY, BLOCK)  //todo Hate It too
                , newAction("sound_effect", ALL, BLOCK) //todo
//                , newAction("randomize_weather", ALL, BLOCK) //todo
//                , newAction("break", ALL, BOOLEAN)
//                , newAction("tdebug", NONE, BOOLEAN)

                // Additional Commands
//                , newAction("limit", INHERITED, BLOCK) //todo
                , newAction("hidden_trigger", INHERITED, BLOCK) //todo
                , newAction("custom_trigger_tooltip", INHERITED, BLOCK) //todo
                , newAction("custom_effect_tooltip", INHERITED, BLOCK) //todo
                , newAction("hidden_effect", INHERITED, BLOCK) //todo
//                , newAction("if", INHERITED, BLOCK) //todo
                , newAction("effect_tooltip", INHERITED, BLOCK) //todo
                , newAction("show_ideas_tooltip", ALL, ID)
                , newAction("add_ai_strategy", COUNTRY, BLOCK) //todo not in wiki
        ));

        NODES.forEach(n -> {
            if (n.isCondition() || (n instanceof BlockNode && ((BlockNode) n).hasConditions() && !"limit".equals(n.getId()))) {
                Map<String, Node> scopedConditions = CONDITIONS_MAP.computeIfAbsent(n.getScope(), nn -> new HashMap<>());
                scopedConditions.put(n.getId().toLowerCase(Locale.ROOT), n);
            }
            if (n.isAction() || (n instanceof BlockNode && ((BlockNode) n).hasActions())) {
                ACTIONS.put(n.getId().toLowerCase(Locale.ROOT), n);
            }
        });
    }
}
