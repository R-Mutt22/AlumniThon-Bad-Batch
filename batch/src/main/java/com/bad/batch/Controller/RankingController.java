package com.bad.batch.Controller;

import com.bad.batch.Model.Ranking;
import com.bad.batch.Enum.RankingType;
import com.bad.batch.Repository.RankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rankings")
public class RankingController {
    @Autowired
    private RankingRepository rankingRepository;

    @GetMapping
    public List<Ranking> getAllRankings() {
        return rankingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ranking> getRankingById(@PathVariable Long id) {
        Optional<Ranking> ranking = rankingRepository.findById(id);
        return ranking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ranking createRanking(@RequestBody Ranking ranking) {
        if (ranking.getType() == null) {
            ranking.setType(RankingType.GENERAL);
        }
        return rankingRepository.save(ranking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ranking> updateRanking(@PathVariable Long id, @RequestBody Ranking details) {
        Optional<Ranking> optional = rankingRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Ranking ranking = optional.get();
        ranking.setType(details.getType());
        ranking.setCategory(details.getCategory());
        ranking.setChallenge(details.getChallenge());
        ranking.setPosition(details.getPosition());
        ranking.setPoints(details.getPoints());
        ranking.setScore(details.getScore());
        return ResponseEntity.ok(rankingRepository.save(ranking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRanking(@PathVariable Long id) {
        if (!rankingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rankingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

