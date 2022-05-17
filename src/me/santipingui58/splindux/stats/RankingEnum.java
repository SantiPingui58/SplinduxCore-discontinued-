package me.santipingui58.splindux.stats;

import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefType;

public enum RankingEnum {
	
	SPLEEF1VS1_WINS,
	SPLEEF1VS1_GAMES,
	SPLEEF1VS1_ELO,
	SPLEEFFFA_WINS,
	SPLEEFFFA_GAMES,
	SPLEEFFFA_KILLS,
	SPLEEFFFA_WINS_MONTHLY,
	SPLEEFFFA_WINS_WEEKLY,
	SPLEEFFFA_GAMES_MONTHLY,
	SPLEEFFFA_GAMES_WEEKLY,
	SPLEEFFFA_KILLS_MONTHLY,
	SPLEEFFFA_KILLS_WEEKLY,

	
	SPLEGG1VS1_WINS,
	SPLEGG1VS1_GAMES,
	SPLEGG1VS1_ELO,
	SPLEGGFFA_WINS,
	SPLEGGFFA_GAMES,
	SPLEGGFFA_KILLS,
	SPLEGGFFA_WINS_MONTHLY,
	SPLEGGFFA_WINS_WEEKLY,
	SPLEGGFFA_GAMES_MONTHLY,
	SPLEGGFFA_GAMES_WEEKLY,
	SPLEGGFFA_KILLS_MONTHLY,
	SPLEGGFFA_KILLS_WEEKLY,
	
	TNTRUN1VS1_WINS,
	TNTRUN1VS1_GAMES,
	TNTRUN1VS1_ELO,
	TNTRUNFFA_WINS,
	TNTRUNFFA_GAMES,
	TNTRUNFFA_KILLS,
	TNTRUNFFA_WINS_MONTHLY,
	TNTRUNFFA_WINS_WEEKLY,
	TNTRUNFFA_GAMES_MONTHLY,
	TNTRUNFFA_GAMES_WEEKLY,
	TNTRUNFFA_KILLS_MONTHLY,
	TNTRUNFFA_KILLS_WEEKLY,
	
	TOTALONLINETIME,
	COINS,
	EXP,
	GUILD_VALUE,
	RANKING,
	VOTES,
	PARKOUR
	;
	
	
	public GameType getGameType() {
		if (this.equals(TOTALONLINETIME) || this.equals(PARKOUR) || this.equals(RANKING)) return null;
		switch (this) {
		case SPLEEF1VS1_WINS: return GameType.DUEL;
		case SPLEEF1VS1_GAMES: return GameType.DUEL;
		case SPLEEF1VS1_ELO: return GameType.DUEL;
		
		case SPLEGG1VS1_WINS: return GameType.DUEL;
		case SPLEGG1VS1_GAMES: return GameType.DUEL;
		case SPLEGG1VS1_ELO: return GameType.DUEL;
		
		case TNTRUN1VS1_WINS: return GameType.DUEL;
		case TNTRUN1VS1_GAMES: return GameType.DUEL;
		case TNTRUN1VS1_ELO: return GameType.DUEL;
		 default: return GameType.FFA;
		}
	}
	
 	
	public SpleefRankingPeriod getSpleefRankingPeriod()	{
		switch(this) {
		case SPLEEFFFA_GAMES:
			return SpleefRankingPeriod.ALL_TIME;
		case SPLEEFFFA_GAMES_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case SPLEEFFFA_GAMES_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		case SPLEEFFFA_KILLS:
			return SpleefRankingPeriod.ALL_TIME;
		case SPLEEFFFA_KILLS_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case SPLEEFFFA_KILLS_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		case SPLEEFFFA_WINS:
			return SpleefRankingPeriod.ALL_TIME;
		case SPLEEFFFA_WINS_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case SPLEEFFFA_WINS_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		case SPLEGGFFA_GAMES:
			return SpleefRankingPeriod.ALL_TIME;
		case SPLEGGFFA_GAMES_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case SPLEGGFFA_GAMES_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		case SPLEGGFFA_KILLS:
			return SpleefRankingPeriod.ALL_TIME;
		case SPLEGGFFA_KILLS_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case SPLEGGFFA_KILLS_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		case SPLEGGFFA_WINS:
			return SpleefRankingPeriod.ALL_TIME;
		case SPLEGGFFA_WINS_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case SPLEGGFFA_WINS_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		case TNTRUNFFA_GAMES:
			return SpleefRankingPeriod.ALL_TIME;
		case TNTRUNFFA_GAMES_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case TNTRUNFFA_GAMES_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		case TNTRUNFFA_KILLS:
			return SpleefRankingPeriod.ALL_TIME;
		case TNTRUNFFA_KILLS_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case TNTRUNFFA_KILLS_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		case TNTRUNFFA_WINS:
			return SpleefRankingPeriod.ALL_TIME;
		case TNTRUNFFA_WINS_MONTHLY:
			return SpleefRankingPeriod.MONTHLY;
		case TNTRUNFFA_WINS_WEEKLY:
			return SpleefRankingPeriod.WEEKLY;
		default:
		return null;
		
		}
	}	
	public SpleefRankingType getSpleefRankingType() {
		switch (this) {
		case PARKOUR:
			break;
		case RANKING:
			break;
		case SPLEEF1VS1_ELO:
			return SpleefRankingType.ELO;
		case SPLEEF1VS1_GAMES:
			return SpleefRankingType.GAMES;
		case SPLEEF1VS1_WINS:
			return SpleefRankingType.WINS;
		case SPLEEFFFA_GAMES:
			return SpleefRankingType.GAMES;
		case SPLEEFFFA_GAMES_MONTHLY:
			return SpleefRankingType.GAMES;
		case SPLEEFFFA_GAMES_WEEKLY:
			return SpleefRankingType.GAMES;
		case SPLEEFFFA_KILLS:
			return SpleefRankingType.KILLS;
		case SPLEEFFFA_KILLS_MONTHLY:
			return SpleefRankingType.KILLS;
		case SPLEEFFFA_KILLS_WEEKLY:
			return SpleefRankingType.KILLS;
		case SPLEEFFFA_WINS:
			return SpleefRankingType.WINS;
		case SPLEEFFFA_WINS_MONTHLY:
			return SpleefRankingType.WINS;
		case SPLEEFFFA_WINS_WEEKLY:
			return SpleefRankingType.WINS;
		case SPLEGG1VS1_ELO:
			return SpleefRankingType.ELO;
		case SPLEGG1VS1_GAMES:
			return SpleefRankingType.GAMES;
		case SPLEGG1VS1_WINS:
			return SpleefRankingType.WINS;
		case SPLEGGFFA_GAMES:
			return SpleefRankingType.GAMES;
		case SPLEGGFFA_GAMES_MONTHLY:
			return SpleefRankingType.GAMES;
		case SPLEGGFFA_GAMES_WEEKLY:
			return SpleefRankingType.GAMES;
		case SPLEGGFFA_KILLS:
			return SpleefRankingType.KILLS;
		case SPLEGGFFA_KILLS_MONTHLY:
			return SpleefRankingType.KILLS;
		case SPLEGGFFA_KILLS_WEEKLY:
			return SpleefRankingType.KILLS;
		case SPLEGGFFA_WINS:
			return SpleefRankingType.WINS;
		case SPLEGGFFA_WINS_MONTHLY:
			return SpleefRankingType.WINS;
		case SPLEGGFFA_WINS_WEEKLY:
			return SpleefRankingType.WINS;
		case TNTRUN1VS1_ELO:
			return SpleefRankingType.ELO;
		case TNTRUN1VS1_GAMES:
			return SpleefRankingType.GAMES;
		case TNTRUN1VS1_WINS:
			return SpleefRankingType.WINS;
		case TNTRUNFFA_GAMES:
			return SpleefRankingType.GAMES;
		case TNTRUNFFA_GAMES_MONTHLY:
			return SpleefRankingType.GAMES;
		case TNTRUNFFA_GAMES_WEEKLY:
			return SpleefRankingType.GAMES;
		case TNTRUNFFA_KILLS:
			return SpleefRankingType.KILLS;
		case TNTRUNFFA_KILLS_MONTHLY:
			return SpleefRankingType.KILLS;
		case TNTRUNFFA_KILLS_WEEKLY:
			return SpleefRankingType.KILLS;
		case TNTRUNFFA_WINS:
			return SpleefRankingType.WINS;
		case TNTRUNFFA_WINS_MONTHLY:
			return SpleefRankingType.WINS;
		case TNTRUNFFA_WINS_WEEKLY:
			return SpleefRankingType.WINS;
		case TOTALONLINETIME:
			break;
		default:
			break;	
		}
		return null;
	}
	
	public SpleefType getSpleefType() {
		switch (this) {
		case PARKOUR:
			break;
		case RANKING:
			break;
		case SPLEEF1VS1_ELO:
			return SpleefType.SPLEEF;
		case SPLEEF1VS1_GAMES:
			return SpleefType.SPLEEF;
		case SPLEEF1VS1_WINS:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_GAMES:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_GAMES_MONTHLY:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_GAMES_WEEKLY:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_KILLS:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_KILLS_MONTHLY:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_KILLS_WEEKLY:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_WINS:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_WINS_MONTHLY:
			return SpleefType.SPLEEF;
		case SPLEEFFFA_WINS_WEEKLY:
			return SpleefType.SPLEEF;
		case SPLEGG1VS1_ELO:
			return SpleefType.SPLEGG;
		case SPLEGG1VS1_GAMES:
			return SpleefType.SPLEGG;
		case SPLEGG1VS1_WINS:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_GAMES:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_GAMES_MONTHLY:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_GAMES_WEEKLY:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_KILLS:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_KILLS_MONTHLY:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_KILLS_WEEKLY:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_WINS:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_WINS_MONTHLY:
			return SpleefType.SPLEGG;
		case SPLEGGFFA_WINS_WEEKLY:
			return SpleefType.SPLEGG;
		case TNTRUN1VS1_ELO:
			return SpleefType.TNTRUN;
		case TNTRUN1VS1_GAMES:
			return SpleefType.TNTRUN;
		case TNTRUN1VS1_WINS:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_GAMES:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_GAMES_MONTHLY:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_GAMES_WEEKLY:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_KILLS:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_KILLS_MONTHLY:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_KILLS_WEEKLY:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_WINS:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_WINS_MONTHLY:
			return SpleefType.TNTRUN;
		case TNTRUNFFA_WINS_WEEKLY:
			return SpleefType.TNTRUN;
		case TOTALONLINETIME:
			break;
		default:
			break;	
		}
		return null;
	}
	
	
	public static RankingEnum getRankingEnum(SpleefType spleefType,GameType gameType,SpleefRankingPeriod periodType,SpleefRankingType type) {
		switch(spleefType) {
		case SPLEEF:
			switch(gameType) {
			case DUEL:
				switch (type) {
				case ELO:
					return RankingEnum.SPLEEF1VS1_ELO;
				case GAMES:
					return RankingEnum.SPLEEF1VS1_GAMES;
				case KILLS:
					break;
				case WINS:
					return RankingEnum.SPLEEF1VS1_WINS;
				}
			case FFA:
				switch (type) {
				case GAMES:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.SPLEEFFFA_GAMES;
					case MONTHLY:
						return RankingEnum.SPLEEFFFA_GAMES_MONTHLY;
					case WEEKLY:
						return RankingEnum.SPLEEFFFA_GAMES_WEEKLY;
					}
					
				case KILLS:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.SPLEEFFFA_KILLS;
					case MONTHLY:
						return RankingEnum.SPLEEFFFA_KILLS_MONTHLY;
					case WEEKLY:
						return RankingEnum.SPLEEFFFA_KILLS_WEEKLY;
					}
				case WINS:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.SPLEEFFFA_WINS;
					case MONTHLY:
						return RankingEnum.SPLEEFFFA_WINS_MONTHLY;
					case WEEKLY:
						return RankingEnum.SPLEEFFFA_WINS_WEEKLY;
					}
				default:
					break;
				}
			default:
				break;
		
			}
		case SPLEGG:
			switch (gameType) {
			case DUEL:
				switch (type) {
				case ELO:
					return RankingEnum.SPLEGG1VS1_ELO;
				case GAMES:
					return RankingEnum.SPLEGG1VS1_GAMES;
				case KILLS:
					break;
				case WINS:
					return RankingEnum.SPLEGG1VS1_WINS;
				}
			case FFA:
				switch (type) {
				case GAMES:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.SPLEGGFFA_GAMES;
					case MONTHLY:
						return RankingEnum.SPLEGGFFA_GAMES_MONTHLY;
					case WEEKLY:
						return RankingEnum.SPLEGGFFA_GAMES_WEEKLY;
					}
					
				case KILLS:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.SPLEGGFFA_KILLS;
					case MONTHLY:
						return RankingEnum.SPLEGGFFA_KILLS_MONTHLY;
					case WEEKLY:
						return RankingEnum.SPLEGGFFA_KILLS_WEEKLY;
					}
				case WINS:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.SPLEGGFFA_WINS;
					case MONTHLY:
						return RankingEnum.SPLEGGFFA_WINS_MONTHLY;
					case WEEKLY:
						return RankingEnum.SPLEGGFFA_WINS_WEEKLY;
					}
				default:
					break;
				}
			default:
				break;
		
			}
		case TNTRUN:
			switch (gameType) {
			case DUEL:
				switch (type) {
				case ELO:
					return RankingEnum.TNTRUN1VS1_ELO;
				case GAMES:
					return RankingEnum.TNTRUN1VS1_GAMES;
				case KILLS:
					break;
				case WINS:
					return RankingEnum.TNTRUN1VS1_WINS;
				}
			case FFA:
				switch (type) {
				case GAMES:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.TNTRUNFFA_GAMES;
					case MONTHLY:
						return RankingEnum.TNTRUNFFA_GAMES_MONTHLY;
					case WEEKLY:
						return RankingEnum.TNTRUNFFA_GAMES_WEEKLY;
					}
					
				case KILLS:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.TNTRUNFFA_KILLS;
					case MONTHLY:
						return RankingEnum.TNTRUNFFA_KILLS_MONTHLY;
					case WEEKLY:
						return RankingEnum.TNTRUNFFA_KILLS_WEEKLY;
					}
				case WINS:
					switch (periodType) {
					case ALL_TIME:
						return RankingEnum.TNTRUNFFA_WINS;
					case MONTHLY:
						return RankingEnum.TNTRUNFFA_WINS_MONTHLY;
					case WEEKLY:
						return RankingEnum.TNTRUNFFA_WINS_WEEKLY;
					}
				default:
					break;
				}
			default:
				break;
		
			}
		default:
			break;
		
		}
		return null;
	}
	
	public String getRowName() {
		
		if (this.equals(PARKOUR)) return "points";
		if (this.equals(TOTALONLINETIME)) return "onlinetime";
		
		SpleefRankingType spleefRankingType = getSpleefRankingType();
		
		if (spleefRankingType.equals(SpleefRankingType.ELO)) return "elo";
		
		GameType gameType = getGameType();
		
		if (gameType.equals(GameType.DUEL)) {
			return "duel_"+spleefRankingType.toString().toLowerCase();
		} else {
			SpleefRankingPeriod spleefRankingPeriod = getSpleefRankingPeriod();
			
			String period = spleefRankingPeriod.equals(SpleefRankingPeriod.ALL_TIME) ? "global" : spleefRankingPeriod.toString().toLowerCase();
			return period+"_ffa_"+spleefRankingType.toString().toLowerCase();
		}
	}
	
	public String getTableName() {
		if (this.equals(PARKOUR)) {
			return "parkour";
		} else if (this.equals(TOTALONLINETIME)){
			return "player_data";
		}  else {
			switch (getSpleefType()) {
			case BOWSPLEEF:
				break;
			case POTSPLEEF:
				break;
			case SPLEEF:
				return "spleef_stats";
			case SPLEGG:
				return "splegg_stats";
			case TNTRUN:
				return "tntrun_stats";
			default:
				break;
			
			}
		}
		return null;
	}
	
	
}
