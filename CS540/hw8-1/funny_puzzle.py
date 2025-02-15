import heapq

def get_manhattan_distance(state, goal_state=[1, 2, 3, 4, 5, 6, 7, 0, 0]):
    """Calculate the Manhattan distance from the current state to the goal state."""
    distance = 0
    side_length = 3
    for idx, tile in enumerate(state):
        if tile != 0:
            target_idx = goal_state.index(tile)
            # Calculate positions (row, col) from indices
            row, col = divmod(idx, side_length)
            target_row, target_col = divmod(target_idx, side_length)
            # Accumulate Manhattan distances
            distance += abs(row - target_row) + abs(col - target_col)
    return distance

def print_succ(state):
    """Print all valid successors of the given state sorted by their 'numerical' values."""
    successors = get_successors(state)
    for s in sorted(successors, key=lambda x: x[1]):
        print(s[1], "h={}".format(get_manhattan_distance(s[1])))

def get_successors(state):
    """Generate all valid successor states by moving the '0' (empty space)."""
    side_length = 3
    successors = []
    zero_index = state.index(0)  # Find the index of the empty tile
    row, col = divmod(zero_index, side_length)
    
    # Possible movements: up, down, left, right
    directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    for dr, dc in directions:
        new_row, new_col = row + dr, col + dc
        if 0 <= new_row < side_length and 0 <= new_col < side_length:
            new_index = new_row * side_length + new_col
            # Create new state by swapping zero
            new_state = state[:]
            new_state[zero_index], new_state[new_index] = new_state[new_index], new_state[zero_index]
            successors.append((zero_index, new_state))
    return successors

def solve(initial_state):
    """Perform A* search algorithm to find the solution from initial state to the goal state."""
    goal_state = [1, 2, 3, 4, 5, 6, 7, 0, 0]
    pq = []
    parent_map = {tuple(initial_state): None}
    heapq.heappush(pq, (get_manhattan_distance(initial_state), 0, initial_state))
    max_queue_length = 0
    
    while pq:
        current_priority, current_g, current_state = heapq.heappop(pq)
        current_h = current_priority - current_g
        
        if current_state == goal_state:
            break
        
        for _, successor in get_successors(current_state):
            g = current_g + 1
            h = get_manhattan_distance(successor)
            heapq.heappush(pq, (g + h, g, successor))
            parent_map[tuple(successor)] = current_state
        
        max_queue_length = max(max_queue_length, len(pq))
    
    # Reconstruct the path
    path = []
    state = tuple(goal_state)
    while state:
        path.append((state, get_manhattan_distance(list(state))))
        state = parent_map[state]
    
    path.reverse()
    for state, h in path:
        moves = len(path) - path.index((state, h)) - 1
        print(list(state), "h={}".format(h), "moves: {}".format(moves))
    print("Max queue length:", max_queue_length)

# Example usage
if __name__ == "__main__":
    
    solve([4,3,0,5,1,6,7,2,0])
