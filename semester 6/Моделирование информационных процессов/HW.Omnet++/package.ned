//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see http://www.gnu.org/licenses/.
//


import org.omnetpp.queueing.Allocate;
import org.omnetpp.queueing.Classifier;
import org.omnetpp.queueing.Deallocate;
import org.omnetpp.queueing.Delay;
import org.omnetpp.queueing.Merge;
import org.omnetpp.queueing.ResourcePool;
import org.omnetpp.queueing.Router;
import org.omnetpp.queueing.Sink;
import org.omnetpp.queueing.Source;

@license(LGPL);

network Network {
    submodules:
        sinkStat: SinkStat { }
        sink: Sink { }
        source: Source { }
        resourcePool: ResourcePool { }
        resourcePool1: ResourcePool { }
        resourcePool2: ResourcePool { }
        allocate: Allocate { }
        allocate1: Allocate { }
        allocate2: Allocate { }
        deallocate: Deallocate { }
        deallocate1: Deallocate { }
        deallocate2: Deallocate { }
        delay: Delay { }
        delay1: Delay { }
        delay2: Delay { }
        router: Router { }
    connections:
        source.out --> allocate.in++;
        allocate.out --> delay.in++;
        delay.out --> deallocate.in++;
        deallocate.out --> allocate1.in++;
        allocate1.out --> delay1.in++;
        delay1.out --> deallocate1.in++;
        deallocate1.out --> allocate2.in++;
        allocate2.out --> delay2.in++;
        delay2.out --> deallocate2.in++;
        deallocate2.out --> router.in++;
        router.out++ --> sinkStat.in++;
        router.out++ --> sink.in++;
}
